package org.com.openmarket.items.application.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.com.openmarket.items.application.config.constants.QueueConstants.UserItem.*;

@Configuration
public class QueueConfig {
    @Bean
    public Queue makeUserDataQueue() {
        return createQueue(USER_DATA_ITEM_QUEUE);
    }

    @Bean
    public TopicExchange makeUserDataExchange() {
        return createExchange(USER_DATA_ITEM_TOPIC_EXCHANGE);
    }

    @Bean
    public Binding makeUserDataBinding() {
        return createBinding(makeUserDataQueue(), makeUserDataExchange(), USER_DATA_ITEM_ROUTING_KEY);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        return container;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public Queue createQueue(String queueName) {
        return new Queue(queueName, true, false, false);
    }

    public TopicExchange createExchange(String exchangeName) {
        return new TopicExchange(exchangeName);
    }

    private Binding createBinding(Queue queue, TopicExchange exchange, String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }
}
