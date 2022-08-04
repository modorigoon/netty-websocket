package me.modorigoon.fun.websocket.server.pubsub;

import java.util.concurrent.CompletableFuture;


/**
 * @author modorigoon
 * @since 2018.
 */
public interface Publisher {

    CompletableFuture<Long> publish(MessageFrame messageFrame);
}
