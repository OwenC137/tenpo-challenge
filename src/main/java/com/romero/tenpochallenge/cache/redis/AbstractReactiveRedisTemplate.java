package com.romero.tenpochallenge.cache.redis;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

abstract class AbstractReactiveRedisTemplate<T> {
    protected final ReactiveRedisTemplate<String, T> reactiveRedisTemplate;

    public AbstractReactiveRedisTemplate(final Class<T> type, final ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        final StringRedisSerializer keySerializer = new StringRedisSerializer();
        final Jackson2JsonRedisSerializer<T> valueSerializer = new Jackson2JsonRedisSerializer<T>(type);
        final RedisSerializationContext.RedisSerializationContextBuilder<String, T> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        final RedisSerializationContext<String, T> context =
                builder.value(valueSerializer).build();
        this.reactiveRedisTemplate = new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, context);
    }
}
