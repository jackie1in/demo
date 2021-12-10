package com.github.silencecorner.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.function.Function;

public class Worker<K,V> implements Runnable{
    private ConsumerRecord<K,V> record;
    private Function<ConsumerRecord<K,V>,Void> work;

    public Worker(ConsumerRecord<K, V> record, Function<ConsumerRecord<K, V>, Void> work) {
        this.record = record;
        this.work = work;
    }

    @Override
    public void run() {
        work.apply(this.record);
    }
}
