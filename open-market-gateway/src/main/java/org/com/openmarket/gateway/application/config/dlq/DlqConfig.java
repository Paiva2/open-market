package org.com.openmarket.gateway.application.config.dlq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.com.openmarket.gateway.application.config.dlq.constants.QueueConstants.Dlq.DEAD_LETTER_QUEUE;
import static org.com.openmarket.gateway.application.config.dlq.constants.QueueConstants.Dlq.DEAD_LETTER_QUEUE_TOPIC_EXCHANGE;

@Configuration
public class DlqConfig {
    @Bean
    public Queue makeDlqQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE)
            .withArgument("x-dead-letter-exchange", DEAD_LETTER_QUEUE_TOPIC_EXCHANGE)
            .build();
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(makeDlqQueue()).to(deadLetterExchange());
    }

    @Bean
    public FanoutExchange deadLetterExchange() {
        return new FanoutExchange(DEAD_LETTER_QUEUE_TOPIC_EXCHANGE);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        return container;
    }
}