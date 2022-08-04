package me.modorigoon.fun.websocket.server.pubsub.provider.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaworks.redis.pubsub.StatefulRedisPubSubConnection;
import me.modorigoon.fun.websocket.server.pubsub.MessageFrame;
import me.modorigoon.fun.websocket.server.pubsub.Publisher;
import me.modorigoon.fun.websocket.server.pubsub.provider.PubSubProviderProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


/**
 * @author modorigoon
 * @since 2018.
 */
@Component
public class RedisPublisher implements Publisher {

    private final StatefulRedisPubSubConnection<String, String> pubRedisConnection;
    private final ObjectMapper objectMapper;
    private final PubSubProviderProperties pubSubProviderProperties;

    public RedisPublisher(StatefulRedisPubSubConnection<String, String> pubRedisConnection,
                          ObjectMapper objectMapper,
                          PubSubProviderProperties pubSubProviderProperties) {
        this.pubRedisConnection = pubRedisConnection;
        this.objectMapper = objectMapper;
        this.pubSubProviderProperties = pubSubProviderProperties;
    }

    @Override
    public CompletableFuture<Long> publish(MessageFrame messageFrame) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        try {
            future = pubRedisConnection.async()
                    .publish(pubSubProviderProperties.getChannel(), objectMapper.writeValueAsString(messageFrame))
                    .toCompletableFuture();
        } catch (JsonProcessingException e) {
            future.completeExceptionally(e);
        }
        return future;
    }
}
