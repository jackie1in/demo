package com.example.rabbit;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
class RabbitApplicationTests {
    private static Logger logger = LoggerFactory.getLogger(RabbitApplicationTests.class);
    @Test
    void contextLoads() {
    }
    @Autowired
    private RetryQueuesInterceptor retryQueues;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void whenSendToNonBlockingQueue_thenAllMessageProcessed() throws Exception {
        int nb = 2;

        CountDownLatch latch = new CountDownLatch(nb);
        retryQueues.setObserver(() -> latch.countDown());

        for (int i = 1; i <= nb; i++) {
            logger.info("send msg: {}","non-blocking message " + i);
            rabbitTemplate.convertAndSend("yinhai-devops-msgconnector", "non-blocking message " + i);
        }

        latch.await();
    }
}
