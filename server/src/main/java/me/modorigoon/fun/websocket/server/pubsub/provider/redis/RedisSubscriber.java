package me.modorigoon.fun.websocket.server.pubsub.provider.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaworks.redis.pubsub.RedisPubSubAdapter;
import com.lambdaworks.redis.pubsub.StatefulRedisPubSubConnection;
import me.modorigoon.fun.websocket.server.pubsub.Dispatcher;
import me.modorigoon.fun.websocket.server.pubsub.MessageFrame;
import me.modorigoon.fun.websocket.server.pubsub.Subscriber;
import me.modorigoon.fun.websocket.server.pubsub.provider.PubSubProviderProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;


/**
 * @author modorigoon
 * @since 2018.
 */
@Component
public class RedisSubscriber implements Subscriber {

    private final StatefulRedisPubSubConnection<String, String> subRedisConnection;
    private final Dispatcher dispatcher;
    private final ObjectMapper objectMapper;
    private final PubSubProviderProperties pubSubProviderProperties;

    Logger log = LoggerFactory.getLogger(RedisSubscriber.class);

    public RedisSubscriber(StatefulRedisPubSubConnection<String, String> subRedisConnection,
                           Dispatcher dispatcher,
                           ObjectMapper objectMapper,
                           PubSubProviderProperties pubSubProviderProperties) {
        this.subRedisConnection = subRedisConnection;
        this.dispatcher = dispatcher;
        this.objectMapper = objectMapper;
        this.pubSubProviderProperties = pubSubProviderProperties;
    }

    @Override
    @PostConstruct
    public void subscribe() {
        subRedisConnection.addListener(new RedisPubSubAdapter<>() {
            @Override
            public void message(String channel, String message) {
                try {
                    dispatcher.dispatch(objectMapper.readValue(message, MessageFrame.class));
                } catch (IOException e) {
                    log.error("[RedisSubscriber:subscribe] error: ", e);
                }
            }
        });
        subRedisConnection.async().subscribe(pubSubProviderProperties.getChannel());
    }
}
