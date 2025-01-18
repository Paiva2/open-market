package org.com.openmarket.market.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
@EnableRedisRepositories
public class CacheConfig {
    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisProperties properties = redisProperties();
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();

        configuration.setPassword(properties.getPassword());
        configuration.setHostName(properties.getHost());
        configuration.setPort(properties.getPort());

        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }

    @Bean
    @Primary
    public RedisProperties redisProperties() {
        RedisProperties properties = new RedisProperties();
        properties.setPassword(password);

        return properties;
    }
}
