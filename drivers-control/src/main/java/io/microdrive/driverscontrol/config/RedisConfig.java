package io.microdrive.driverscontrol.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveSetOperations;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    ReactiveSetOperations<String, String> reactiveSetOperations(ReactiveRedisConnectionFactory cf) {
        RedisSerializationContext<String, String> context = RedisSerializationContext
            .<String, String>newSerializationContext(new StringRedisSerializer())
            .key(new StringRedisSerializer())
            .value(new StringRedisSerializer())
            .hashKey(new StringRedisSerializer())
            .hashKey(new StringRedisSerializer())
            .build();

        return new ReactiveRedisTemplate<>(cf, context).opsForSet();
    }
}
