package com.github.silencecorner.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class ConsumerWorkHandler<K, V> implements Runnable {
    private final AtomicBoolean closed = new AtomicBoolean(false);

    private Consumer<K, V> consumer;

    private ExecutorService executorService;

    private boolean autoCommit;

    private Duration pollMs;

    private long doWorkMs;

    private Function<ConsumerRecord<K, V>, Void> work;

    public static ConsumerWorkHandler<Integer, Integer> defaultHandler(String bootstrapServers,
                                                                       int workPoolSize,
                                                                       long pollMs,
                                                                       long doWorkMs,
                                                                       Function<ConsumerRecord<Integer, Integer>, Void> work) {
        return new ConsumerWorkHandler<>(bootstrapServers,
                new IntegerDeserializer(),
                new IntegerDeserializer(),
                "handler-group-id",
                "handler-consumer-id",
                false,
                true,
                1000,
                workPoolSize,
                pollMs,
                1000,
                1000,
                doWorkMs,
                work);
    }

    public ConsumerWorkHandler(String bootstrapServers,
                               Deserializer<K> keyDeserializer,
                               Deserializer<V> valueDeserializer,
                               String groupId,
                               String clientId,
                               boolean transaction,
                               boolean autoCommit,
                               Integer autoCommitIntervalMs,
                               int workPoolSize,
                               long pollMs,
                               long maxPollIntervalMs,
                               long maxPollRecords,
                               long doWorkMs,
                               Function<ConsumerRecord<K, V>, Void> work) {

        Properties props = new Properties();
        // 这里可以不用写完
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        if (clientId != null) {
            props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        }
        if (transaction) {
            // 设置数据可见行
            props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        }
        // 太小会触发rebalance
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords > 1000 ? 1000 : maxPollRecords);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
        if (autoCommit) {
            props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitIntervalMs);
        }
        this.autoCommit = autoCommit;
        this.consumer = new KafkaConsumer<>(props, keyDeserializer, valueDeserializer);
        this.pollMs = Duration.ofMillis(pollMs);
        this.doWorkMs = doWorkMs;
        this.work = work;
        this.executorService = new ThreadPoolExecutor(workPoolSize,
                Runtime.getRuntime().availableProcessors(),
                0,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000));
    }

    public void assign(Collection<TopicPartition> partitions) {
        consumer.assign(partitions);
    }

    public void subscribe(Collection<String> topic) {
        consumer.subscribe(topic);
    }

    public void shutdown() {
        if (consumer != null) {
            // consumer.close();
            closed.set(true);
            consumer.wakeup();
        }
        if (executorService != null) {
            try {
                executorService.shutdown();
                if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
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

    @Override
    public void run() {
        try {
            while (!closed.get()) {
                ConsumerRecords<K, V> records = consumer.poll(pollMs);
                // commit offset
                if (!autoCommit) {
                    for (TopicPartition partition : records.partitions()) {
                        List<ConsumerRecord<K, V>> partitionRecords = records.records(partition);
                        if (records.isEmpty()) {
                            continue;
                        }
                        CountDownLatch latch = new CountDownLatch(partitionRecords.size());
                        for (ConsumerRecord<K, V> record : partitionRecords) {
                            //TODO 限速，避免reject
                            executorService.execute(new Worker<>(record, work.andThen((v) -> {
                                latch.countDown();
                                return null;
                            })));
                        }
                        long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                        try {
                            latch.await(doWorkMs, TimeUnit.MILLISECONDS);
                            consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset)));
                        } catch (KafkaException | InterruptedException e) { // 同步所有返回结果时超时,提交任务时发生异常
                            consumer.seek(partition, lastOffset);
                        }
                    }
                } else {
                    for (ConsumerRecord<K, V> record : records) {
                        executorService.execute(new Worker<>(record, work));
                    }
                }
            }
        }catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get()) throw e;
        } finally {
            consumer.close();
        }
    }
}


