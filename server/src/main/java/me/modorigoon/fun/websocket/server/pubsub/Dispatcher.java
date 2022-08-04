package me.modorigoon.fun.websocket.server.pubsub;


/**
 * @author modorigoon
 * @since 2018.
 */
public interface Dispatcher {

    void dispatch(MessageFrame messageFrame);
}
