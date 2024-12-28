package org.com.openmarket.market.application.config;

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

import java.util.HashMap;
import java.util.Map;

import static org.com.openmarket.market.application.config.constants.QueueConstants.Dlq.DEAD_LETTER_QUEUE;
import static org.com.openmarket.market.application.config.constants.QueueConstants.MARKET_QUEUE;
import static org.com.openmarket.market.application.config.constants.QueueConstants.MarketUserData.*;

@Configuration
public class QueueConfig {
    @Bean
    public Queue marketQueue() {
        return createQueue(MARKET_QUEUE);
    }

    @Bean
    public Queue marketUserDataQueue() {
        return createQueue(USER_DATA_MARKET_QUEUE);
    }

    @Bean
    public TopicExchange marketUserDataExchange() {
        return createExchange(USER_DATA_MARKET_TOPIC_EXCHANGE);
    }

    @Bean
    public Binding marketUserDataBinding() {
        return createBinding(marketUserDataQueue(), marketUserDataExchange(), USER_DATA_MARKET_ROUTING_KEY);
    }

    private Queue createQueue(String queueName) {
        Map<String, Object> arguments = new HashMap<>() {{
            put("x-dead-letter-exchange", "");
            put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE);
        }};

        return new Queue(queueName, true, false, false, arguments);
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

    public TopicExchange createExchange(String exchangeName) {
        return new TopicExchange(exchangeName);
    }

    private Binding createBinding(Queue queue, TopicExchange exchange, String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }
}
