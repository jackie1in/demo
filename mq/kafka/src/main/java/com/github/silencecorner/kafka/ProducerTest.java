package com.github.silencecorner.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerTest {
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(), new NamedThreadFactory("kafka-producer"));

    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger num = new AtomicInteger();
        Producer<Integer, Integer> atMostOnceProducer = atMostOnce();
        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            atMostOnceProducer.send(new ProducerRecord<>(Common.AT_MOST_ONCE.get(0), num.getAndIncrement(), num.getAndIncrement()));
        }, 0, 200, TimeUnit.MILLISECONDS);
//        Producer<Integer, Integer> atLeastOnceProducer = atLeastOnce();
//        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
//            atLeastOnceProducer.send(new ProducerRecord<>(Common.AT_LEAST_ONCE.get(0), num.getAndIncrement(), num.getAndIncrement()));
//        }, 0, 200, TimeUnit.MILLISECONDS);
//
//        Producer<Integer, Integer> transactionProducer = exactlyOnceAndTransaction();
//        transactionProducer.initTransactions();
//        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
//            try {
//                transactionProducer.beginTransaction();
//                transactionProducer.send(new ProducerRecord<>(Common.EXACTLY_ONCE_AND_TRANSACTION.get(0), num.getAndIncrement(), num.getAndIncrement()));
//                transactionProducer.commitTransaction();
//            }catch (KafkaException e){
//                e.printStackTrace();
//                transactionProducer.abortTransaction();
//            }
//
//        }, 0, 1, TimeUnit.SECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            atMostOnceProducer.close();
//            atLeastOnceProducer.close();
//            transactionProducer.close();
            latch.countDown();
        }));
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static Producer<Integer, Integer> atMostOnce() {
        Properties props = commonConfig();
        // 这里可以不用写完
        // acks -1/all 0 1 -1/all用于事务消息与retries=0配合使用，等待回复ack给producer，exactly once
        // 0不用等待isr且不判断恢复可以配置retries > 0消息可能重复 at most once
        // 1 至少等带isr 1并回复ack给producer at least once
        props.put("acks", "0");
        // 重试次数
        props.put("retries", 1);
        // partition缓存大小数据字节为单位，每个分区都有一个缓存
        props.put("batch.size", 16384);
        // 数据在producer缓冲区逗留时间，0不逗留
        props.put("linger.ms", 1);
        // 总缓存空间大小，字节单位，也就是未发送到leader缓存总量
        props.put("buffer.memory", 33554432);
        return new KafkaProducer<>(props);
    }

    public static Producer<Integer, Integer> atLeastOnce() {
        Properties props = commonConfig();
        // 这里可以不用写完
        // acks -1/all 0 1 -1/all用于事务消息与retries=0配合使用，等待回复ack给producer，exactly once
        // 0不用等待isr且不判断恢复可以配置retries > 0消息可能重复 at most once
        // 1 至少等带isr 1并回复ack给producer at least once
        props.put("acks", "1");
        // 重试次数
        props.put("retries", 3);
        // partition缓存大小数据字节为单位，每个分区都有一个缓存
        props.put("batch.size", 16384);
        // 数据在producer缓冲区逗留时间，0不逗留
        props.put("linger.ms", 1);
        // 总缓存空间大小，字节单位，也就是未发送到leader缓存总量
        props.put("buffer.memory", 33554432);
        return new KafkaProducer<>(props);
    }

    public static Producer<Integer, Integer> exactlyOnceAndTransaction() {
        Properties props = commonConfig();
        // 这里可以不用写完
        // acks -1/all 0 1 -1/all用于事务消息与retries=0配合使用，等待回复ack给producer，exactly once
        // 0不用等待isr且不判断恢复可以配置retries > 0消息可能重复 at most once
        // 1 至少等带isr 1并回复ack给producer at least once
        props.put("acks", "all");
        // 重试次数, 因为支持幂等必须大于0
        props.put("retries", 3);
        // 指定 transactional.id
        props.put("transactional.id", Common.EXACTLY_ONCE_AND_TRANSACTION.get(0));
        // partition缓存大小数据字节为单位，每个分区都有一个缓存
        props.put("batch.size", 16384);
        // 数据在producer缓冲区逗留时间，0不逗留
        props.put("linger.ms", 1);
        // 总缓存空间大小，字节单位，也就是未发送到leader缓存总量
        props.put("buffer.memory", 33554432);
        return new KafkaProducer<>(props);
    }


    private static Properties commonConfig(){
        Properties props = new Properties();
        // 这里可以不用写完
        props.put("bootstrap.servers","localhost:9092,localhost:9093,localhost:9094,localhost:9095,localhost:9096");
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        return props;

    }
}
