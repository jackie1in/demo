package com.example.rabbit;

import com.example.rabbit.dto.Msg;
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
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;

import java.time.Duration;
import java.util.List;

@Configuration
public class RabbitConfiguration {

    private static Logger logger = LoggerFactory.getLogger(RabbitConfiguration.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Autowired
    private ConfigurableBeanFactory beanFactory;
    // now 5s 30m 2h 1d
    //private Duration[] delay = {Duration.ofSeconds(5), Duration.ofMinutes(30), Duration.ofHours(2), Duration.ofDays(1)};
    private Duration[] delay = {Duration.ofSeconds(5), Duration.ofSeconds(5), Duration.ofSeconds(5), Duration.ofSeconds(10)};
    @PostConstruct
    public void createQueues(){
        for (int i = 0; i < delay.length; i++) {
            beanFactory.registerSingleton("retryQueue" + i,
                    QueueBuilder.durable("retry-queue-" + i)
                            .deadLetterExchange("")
                            .deadLetterRoutingKey("retry-wait-ended-queue")
                            .build());
        }
    }


    @Bean
    public RetryQueues retryQueues(List<Queue> queues) {
        return new RetryQueues(delay, queues.stream().filter(queue -> queue.getName().startsWith("retry-queue-")).toArray(Queue[]::new));
    }

    @Bean
    public Queue connectorQueue() {
        return QueueBuilder.durable("yinhai-devops-msgconnector")
                .build();
    }

    @Bean
    public Queue retryWaitEndedQueue() {
        return QueueBuilder.durable("retry-wait-ended-queue")
                .build();
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
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public SimpleRabbitListenerContainerFactory defaultContainerFactory(ConnectionFactory connectionFactory, MessageConverter jackson2JsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jackson2JsonMessageConverter);

        return factory;
    }


    @Bean
    public SimpleRabbitListenerContainerFactory retryQueuesContainerFactory(ConnectionFactory connectionFactory, RetryQueuesInterceptor retryInterceptor,MessageConverter jackson2JsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jackson2JsonMessageConverter);

        Advice[] adviceChain = {retryInterceptor};
        factory.setAdviceChain(adviceChain);

        return factory;
    }


    @RabbitListener(queues = "yinhai-devops-msgconnector", containerFactory = "retryQueuesContainerFactory", ackMode = "MANUAL")
    public void consumeNonBlocking(Msg msg) throws Exception {
        logger.info("Processing message from yinhai-devops-msgconnector: {}", msg.toString());

        throw new Exception("Error occured!");
    }

    @RabbitListener(queues = "retry-wait-ended-queue", containerFactory = "defaultContainerFactory")
    public void consumeRetryWaitEndedMessage(Message message, Channel channel) throws Exception {
        MessageProperties props = message.getMessageProperties();

        rabbitTemplate.convertAndSend(props.getHeader("x-original-exchange"), props.getHeader("x-original-routing-key"), message);
    }
}
