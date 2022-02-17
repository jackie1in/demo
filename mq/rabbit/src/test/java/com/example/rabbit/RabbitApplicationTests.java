package com.example.rabbit;

import com.example.rabbit.dto.CallMethod;
import com.example.rabbit.dto.Msg;
import com.example.rabbit.dto.OogeMsg;
import com.example.rabbit.dto.OogeOperation;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        int nb = 10;

        CountDownLatch latch = new CountDownLatch(nb);
        retryQueues.setObserver(() -> latch.countDown());

        for (int i = 1; i <= nb; i++) {
            Msg msg = Msg.builder()
                    .channel(Msg.Channel.APP)
                    .to("1")
                    .title("sb")
                    .content("sb")
                    .data("sb")
                    .oogeMsg(OogeMsg.builder()
                            .oogeOperation(OogeOperation
                                    .builder()
                                    .hintText("xxxx")
                                    .callMethod(CallMethod.builder().method(CallMethod.HttpMethod.POST).build())
                                    .build())
                            .operateHint("打开百度")
                            .deepLink("http://www.baidu.com")
                            .build()).build();
            rabbitTemplate.convertAndSend("yinhai-devops-msgconnector", msg);
        }

        latch.await();
    }
}
