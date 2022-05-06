package com.github.silencecorner.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG;

public class ConsumerTest {
    private final static Logger logger = LoggerFactory.getLogger(ConsumerTest.class);
    // 不实用线程池运行，这些线程需要一直执行，使用线程池属于浪费，只有当进程kill时会被关闭
    // private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new NamedThreadFactory("kafka-consumer"));
    private static AtomicBoolean closed = new AtomicBoolean(false);

    public static void main(String[] args) throws InterruptedException {
        // int consumerCount = 5;
        ConsumerWorkHandler<?, ?> consumerWorkHandler = ConsumerTest.atMostOnce();
//        for (int i = 0; i < consumerCount; i++) {
//            EXECUTOR_SERVICE.execute(ConsumerTest::atMostOnce);
//            EXECUTOR_SERVICE.execute(ConsumerTest::atLeastOnceSync);
//            EXECUTOR_SERVICE.execute(ConsumerTest::exactlyOnceAndTransactionAndCommitSync);
//        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            closed.set(true);
            consumerWorkHandler.shutdown();
            countDownLatch.countDown();
            logger.info("shutdown...");
        }));
        countDownLatch.await();
    }

    public static ConsumerWorkHandler<?, ?> atMostOnce() {
        ConsumerWorkHandler<Integer, Integer> consumer = ConsumerWorkHandler.defaultHandler("localhost:9092,localhost:9093",
                5,
                10000,
                2000,
                record -> {
                    System.out.printf("thread = %s topic = %s partition = %s offset = %s value=%s \n",
                            Thread.currentThread().getName(),
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.value());
                    return null;
                });
        consumer.subscribe(Common.AT_MOST_ONCE);
        new Thread(consumer).start();
        return consumer;

    }

    public static Consumer<?, ?> atLeastOnceSync() {
        Properties props = commonConfig();
        props.put("group.id", "test");
        props.put("enable.auto.commit", false);
        Consumer<Integer, Integer> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Common.AT_LEAST_ONCE);
        new Thread(() -> {
            while (!closed.get()) {
                // poll所有消息
                ConsumerRecords<Integer, Integer> records = consumer.poll(Duration.ofSeconds(3));
                if (records.isEmpty()) {
                    continue;
                }
                for (ConsumerRecord<Integer, Integer> record : records) {
                    System.out.printf("thread = %s topic = %s partition = %s offset = %s key=%s value=%s \n",
                            Thread.currentThread().getName(),
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value());
                }
                consumer.commitSync();
            }
        }).start();
        return consumer;
    }

    public static Consumer<?, ?> exactlyOnceAndTransactionAndCommitSync() {
        Properties props = commonConfig();
        props.put("group.id", "test");
        props.put("enable.auto.commit", false);
        // 设置数据可见行
        props.put("isolation.level", "read_committed");

        Consumer<Integer, Integer> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Common.EXACTLY_ONCE_AND_TRANSACTION);
        new Thread(() -> {
            while (!closed.get()) {
                // poll所有消息
                ConsumerRecords<Integer, Integer> records = consumer.poll(Duration.ofSeconds(3));
                if (records.isEmpty()) {
                    continue;
                }
                for (ConsumerRecord<Integer, Integer> record : records) {
                    System.out.printf("thread = %s topic = %s partition = %s offset = %s key=%s value=%s \n",
                            Thread.currentThread().getName(),
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value());
                }
                consumer.commitSync();
            }
        }).start();
        return consumer;
    }

    private static Properties commonConfig() {
        Properties props = new Properties();
        // 这里可以不用写完
        props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094,localhost:9095,localhost:9096");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(MAX_POLL_RECORDS_CONFIG, 1000);
        return props;

    }
}
