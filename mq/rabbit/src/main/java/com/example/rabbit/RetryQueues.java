package com.example.rabbit;

import org.springframework.amqp.core.Queue;

import java.time.Duration;

public class RetryQueues {
    private Queue[] queues;
    private Duration[] delay;

    public RetryQueues(Duration[] delayMills, Queue... queues) {
        if (delayMills.length != queues.length) {
            throw new IllegalArgumentException("重试队列和时间长度需保持一致");
        }
        this.queues = queues;
        this.delay = delayMills;
    }

    public boolean retriesExhausted(int retry) {
        return retry >= queues.length;
    }

    public String getQueueName(int retry) {
        return queues[retry].getName();
    }

    public long getDelayMills(int retry) {
        if (retry >= delay.length) {
            return -1;
        }

        return delay[retry].toMillis();
    }
}