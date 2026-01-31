package me.modorigoon.fun.websocket.server;

import io.netty.channel.Channel;
import me.modorigoon.fun.websocket.server.netty.ChannelGroupManager;
import me.modorigoon.fun.websocket.server.netty.ChannelManager;
import me.modorigoon.fun.websocket.server.pubsub.MessageFrame;
import me.modorigoon.fun.websocket.server.pubsub.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


/**
 * @author modorigoon
 * @since 2018.
 */
@Component
public class WebSocketTemplate {

    private final ChannelGroupManager channelGroupManager;
    private final ChannelManager channelManager;
    private final Publisher publisher;

    Logger log = LoggerFactory.getLogger(WebSocketTemplate.class);

    public WebSocketTemplate(ChannelGroupManager channelGroupManager, ChannelManager channelManager,
                             Publisher publisher) {
        this.channelGroupManager = channelGroupManager;
        this.channelManager = channelManager;
        this.publisher = publisher;
    }

    private void send(MessageFrame frame) {
        CompletableFuture<Long> future = publisher.publish(frame);
        future.exceptionally((Throwable t) -> {
            log.error("[WebSocketTemplate:send] error: ", t);
            return null;
        });
    }

    public void send(String dest, String message, Object data) {
        send(new MessageFrame(MessageFrame.ChannelType.SINGLE, dest, message, data));
    }

    public boolean isRegistered(String name) {
        return channelManager.contains(name);
    }

    public Channel join(String name, Channel channel) {
        return channelManager.add(name, channel);
    }

    public Channel leave(Channel channel) {
        return channelManager.remove(channel);
    }

    public void sendToGroup(String name, String message, Object data) {
        send(new MessageFrame(MessageFrame.ChannelType.GROUP, name, message, data));
    }

    public boolean joinGroup(String name, Channel channel) {
        return channelGroupManager.getOrCreate(name).add(channel);
    }

    public boolean leaveGroup(String name, Channel channel) {
        return channelGroupManager.removeChannelInGroup(name, channel);
    }
}
