package com.example.rabbit;

import com.rabbitmq.client.Channel;
import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class RabbitConfiguration {

    private static Logger logger = LoggerFactory.getLogger(RabbitConfiguration.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public Queue connectorQueue() {
        return QueueBuilder.nonDurable("yinhai-devops-msgconnector")
                .build();
    }

    @Bean
    public Queue retryWaitEndedQueue() {
        return QueueBuilder.nonDurable("retry-wait-ended-queue")
                .build();
    }

    @Bean
    public Queue retryQueue1() {
        return QueueBuilder.nonDurable("retry-queue-1")
                .deadLetterExchange("")
                .deadLetterRoutingKey("retry-wait-ended-queue")
                .build();
    }

    @Bean
    public Queue retryQueue2() {
        return QueueBuilder.nonDurable("retry-queue-2")
                .deadLetterExchange("")
                .deadLetterRoutingKey("retry-wait-ended-queue")
                .build();
    }

    @Bean
    public Queue retryQueue3() {
        return QueueBuilder.nonDurable("retry-queue-3")
                .deadLetterExchange("")
                .deadLetterRoutingKey("retry-wait-ended-queue")
                .build();
    }

    @Bean
    public Queue retryQueue4() {
        return QueueBuilder.nonDurable("retry-queue-3")
                .deadLetterExchange("")
                .deadLetterRoutingKey("retry-wait-ended-queue")
                .build();
    }

    @Bean
    // now 5s 15s 45s 135s(2分15秒）
    public RetryQueues retryQueues(@Qualifier("retryQueue1") Queue retryQueue1, @Qualifier("retryQueue2") Queue retryQueue2, @Qualifier("retryQueue3") Queue retryQueue3, @Qualifier("retryQueue4") Queue retryQueue4) {
        return new RetryQueues(5000, 3, 136000, retryQueue1, retryQueue2, retryQueue3, retryQueue4);
    }

    @Bean
    public ObservableRejectAndDontRequeueRecoverer observableRecoverer() {
        return new ObservableRejectAndDontRequeueRecoverer();
    }

    @Bean
    @DependsOn("retryQueues")
    public RetryQueuesInterceptor retryQueuesInterceptor(RetryQueues retryQueues) {
        return new RetryQueuesInterceptor(rabbitTemplate, retryQueues);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory defaultContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        return factory;
    }


    @Bean
    public SimpleRabbitListenerContainerFactory retryQueuesContainerFactory(ConnectionFactory connectionFactory, RetryQueuesInterceptor retryInterceptor) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        Advice[] adviceChain = {retryInterceptor};
        factory.setAdviceChain(adviceChain);

        return factory;
    }


    @RabbitListener(queues = "yinhai-devops-msgconnector", containerFactory = "retryQueuesContainerFactory", ackMode = "MANUAL")
    public void consumeNonBlocking(String payload) throws Exception {
        logger.info("Processing message from yinhai-devops-msgconnector: {}", payload);

        throw new Exception("Error occured!");
    }

    @RabbitListener(queues = "retry-wait-ended-queue", containerFactory = "defaultContainerFactory")
    public void consumeRetryWaitEndedMessage(String payload, Message message, Channel channel) throws Exception {
        MessageProperties props = message.getMessageProperties();

        rabbitTemplate.convertAndSend(props.getHeader("x-original-exchange"), props.getHeader("x-original-routing-key"), message);
    }
}
