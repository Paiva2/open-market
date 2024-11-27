package org.com.openmarket.users.application.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.com.openmarket.users.application.config.constants.QueueConstants.User.*;

@Configuration
public class QueueConfig {
    @Bean
    public Queue makeUserCreatedQueue() {
        return createQueue(USER_CREATED_QUEUE);
    }

    @Bean
    public TopicExchange makeUserCreatedExchange() {
        return createExchange(USER_CREATED_TOPIC_EXCHANGE);
    }

    @Bean
    public Binding makeUserCreatedBinding() {
        return createBinding(makeUserCreatedQueue(), makeUserCreatedExchange(), USER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue makeUserUpdatedQueue() {
        return createQueue(USER_UPDATED_QUEUE);
    }

    @Bean
    public TopicExchange makeUserUpdatedExchange() {
        return createExchange(USER_UPDATED_TOPIC_EXCHANGE);
    }

    @Bean
    public Binding makeUserUpdatedBinding() {
        return createBinding(makeUserUpdatedQueue(), makeUserUpdatedExchange(), USER_UPDATED_ROUTING_KEY);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        return container;
    }

    private Queue createQueue(String queueName) {
        return new Queue(queueName, true, false, false);
    }

    private TopicExchange createExchange(String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }

    private Binding createBinding(Queue queue, TopicExchange exchange, String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }
}
