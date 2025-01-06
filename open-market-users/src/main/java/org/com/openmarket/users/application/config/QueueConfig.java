package org.com.openmarket.users.application.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.com.openmarket.users.application.config.constants.QueueConstants.Dlq.DEAD_LETTER_QUEUE;
import static org.com.openmarket.users.application.config.constants.QueueConstants.User.USER_QUEUE;

@Configuration
public class QueueConfig {
    @Bean
    public Queue makeUserDataQueue() {
        return createQueue(USER_QUEUE);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        return container;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    private Queue createQueue(String queueName) {
        Map<String, Object> arguments = new HashMap<>() {{
            put("x-dead-letter-exchange", "");
            put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE);
        }};

        return new Queue(queueName, true, false, false, arguments);
    }
}
