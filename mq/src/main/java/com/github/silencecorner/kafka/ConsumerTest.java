package com.github.silencecorner.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerTest {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new NamedThreadFactory("kafka-consumer"));

    public static void main(String[] args) {
        int consumerCount = 5;
        final CountDownLatch latch = new CountDownLatch(1);
        EXECUTOR_SERVICE.execute(ConsumerTest::atMostOnce);
//        for (int i = 0; i < consumerCount; i++) {
//            EXECUTOR_SERVICE.execute(ConsumerTest::atMostOnce);
//            EXECUTOR_SERVICE.execute(ConsumerTest::atLeastOnceSync);
//            EXECUTOR_SERVICE.execute(ConsumerTest::exactlyOnceAndTransactionAndCommitSync);
//        }
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                latch.countDown();
            }));
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void atMostOnce() {
        Properties props = commonConfig();
        props.put("group.id", "test");
        props.put("enable.auto.commit", true);
        props.put("auto.commit.interval.ms", 1000);
        ConsumerWorkHandler<String, String> consumer = ConsumerWorkHandler.defaultHandler("localhost:9092,localhost:9093,localhost:9094,localhost:9095,localhost:9096", 5);
        final CountDownLatch latch = new CountDownLatch(1);

        while (latch.getCount() > 0) {
            // 消费所有的partition上的消息，在客户端上会收到所有partition leader上的消息，消息不会重复但是会乱序
            consumer.subscribe(Common.AT_MOST_ONCE);
            // 消费一个partition上的消息不会乱序，顺序消费最好不要使用多线程
            // consumer.assign(Collections.singleton(new TopicPartition(Common.AT_MOST_ONCE.get(0), 0)));
            consumer.execute(Duration.ofSeconds(10), 1000, record -> {
                System.out.printf("thread = %s topic = %s partition = %s offset = %s key=%s value=%s \n",
                        Thread.currentThread().getName(),
                        record.topic(),
                        record.partition(),
                        record.offset(),
                        record.key(),
                        record.value());
                return null;
            });
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consumer.shutdown();
            latch.countDown();
        }));
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void atLeastOnceSync() {
        Properties props = commonConfig();
        props.put("group.id", "test");
        props.put("enable.auto.commit", false);
        Consumer<Integer, Integer> consumer = new KafkaConsumer<>(props);
        final CountDownLatch latch = new CountDownLatch(1);

        while (latch.getCount() > 0) {
            consumer.subscribe(Common.AT_LEAST_ONCE);
            // poll所有消息
            ConsumerRecords<Integer, Integer> records = consumer.poll(Duration.ofSeconds(3));
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
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consumer.close();
            latch.countDown();
        }));
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void exactlyOnceAndTransactionAndCommitSync() {
        Properties props = commonConfig();
        props.put("group.id", "test");
        props.put("enable.auto.commit", false);
        // 设置数据可见行
        props.put("isolation.level", "read_committed");

        Consumer<Integer, Integer> consumer = new KafkaConsumer<>(props);
        final CountDownLatch latch = new CountDownLatch(1);

        while (latch.getCount() > 0) {
            consumer.subscribe(Common.EXACTLY_ONCE_AND_TRANSACTION);
            // poll所有消息
            ConsumerRecords<Integer, Integer> records = consumer.poll(Duration.ofSeconds(3));
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
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consumer.close();
            latch.countDown();
        }));
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Properties commonConfig() {
        Properties props = new Properties();
        // 这里可以不用写完
        props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094,localhost:9095,localhost:9096");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        return props;

    }
}
