package com.github.silencecorner.kafka;

import com.sun.source.doctree.SinceTree;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.utils.CollectionUtils;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.function.Function;

public class ConsumerWorkHandler<K, V> {
    private Consumer<K, V> consumer;

    private ExecutorService executorService;

    private boolean autoCommit;

    public static ConsumerWorkHandler defaultHandler(String bootstrapServers, int workPoolSize) {

        return new ConsumerWorkHandler(bootstrapServers,
                new IntegerDeserializer(),
                new IntegerDeserializer(),
                "handler-group-id",
                false,
                true,
                1000,
                workPoolSize);
    }

    public ConsumerWorkHandler(String bootstrapServers,
                               Deserializer<K> keyDeserializer,
                               Deserializer<V> valueDeserializer,
                               String groupId,
                               boolean transaction,
                               boolean autoCommit,
                               Integer autoCommitIntervalMs,
                               int workPoolSize) {

        Properties props = new Properties();
        // 这里可以不用写完
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", groupId);
        if (transaction) {
            // 设置数据可见行
            props.put("isolation.level", "read_committed");
        }
        props.put("enable.auto.commit", autoCommit);
        if (autoCommit) {
            props.put("auto.commit.interval.ms", autoCommitIntervalMs);
        }
        this.autoCommit = autoCommit;
        this.consumer = new KafkaConsumer<>(props, keyDeserializer, valueDeserializer);
        this.executorService = new ThreadPoolExecutor(workPoolSize,
                Runtime.getRuntime().availableProcessors(),
                0,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000));
    }

    public void execute(Duration duration, long doWorkMs, Function<ConsumerRecord<K, V>, Void> work) {
        ConsumerRecords<K, V> records = consumer.poll(duration);
        // commit offset
        if (!autoCommit) {
            for (TopicPartition partition : records.partitions()) {
                List<ConsumerRecord<K, V>> partitionRecords = records.records(partition);
                if (records.isEmpty()){
                    continue;
                }
                CountDownLatch latch = new CountDownLatch(partitionRecords.size());
                for (ConsumerRecord<K, V> record : partitionRecords) {
                    executorService.execute(new Worker<>(record, work.andThen((v) -> {
                        latch.countDown();
                        return null;
                    })));
                }
                try {
                    latch.await(doWorkMs, TimeUnit.MILLISECONDS);
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                } catch (InterruptedException e) {
                    // 如果其中一个失败了，这一批都重新消费，提交第一个offset - 1信息
                    long lastOffset = partitionRecords.get(0).offset() - 1;
                    if (lastOffset < 0){
                        // 小于就不提交了
                        continue;
                    }
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                }
            }
        } else {
            for (ConsumerRecord<K, V> record : records) {
                executorService.execute(new Worker<>(record, work));
            }
        }
    }

    public void assign(Collection<TopicPartition> partitions){
        consumer.assign(partitions);
    }

    public void subscribe(Collection<String> topic){
        consumer.subscribe(topic);
    }

    public void shutdown(){
        if (consumer != null){
            consumer.close();
        }
        if (executorService != null){
            try {
                executorService.shutdown();
                if (!executorService.awaitTermination(1,TimeUnit.MINUTES)){
                    // Cancel currently executing tasks
                    executorService.shutdownNow();
                    if (!executorService.awaitTermination(1, TimeUnit.MINUTES))
                        System.err.println("Pool did not terminate");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                // Cancel if current thread also interrupted
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}


