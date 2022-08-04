package me.modorigoon.fun.websocket.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.modorigoon.fun.websocket.server.netty.ChannelGroupManager;
import me.modorigoon.fun.websocket.server.netty.ChannelManager;
import me.modorigoon.fun.websocket.server.pubsub.Dispatcher;
import me.modorigoon.fun.websocket.server.pubsub.MessageFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author modorigoon
 * @since 2018.
 */
@Component
public class WebSocketDispatcher implements Dispatcher {

    private final ChannelGroupManager channelGroupManager;
    private final ChannelManager channelManager;
    private final ObjectMapper objectMapper;

    Logger log = LoggerFactory.getLogger(WebSocketDispatcher.class);

    public WebSocketDispatcher(ChannelGroupManager channelGroupManager,
                               ChannelManager channelManager,
                               ObjectMapper objectMapper) {
        this.channelGroupManager = channelGroupManager;
        this.channelManager = channelManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public void dispatch(MessageFrame messageFrame) {
        switch (messageFrame.getChannelType()) {
            case GROUP:
                group(messageFrame);
                break;
            case SINGLE:
                single(messageFrame);
                break;
            default:
                throw new IllegalArgumentException("Unsupported channel type.");
        }
    }

    private void group(MessageFrame frame) {
        try {
            ChannelGroup group = channelGroupManager.getOrCreate(frame.getDestination());
            ResponseEntity response = new ResponseEntity(frame.getDestination(), frame.getMessage(), frame.getData());
            group.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(response)));
        } catch (JsonProcessingException e) {
            log.error("[WebSocketDispatcher:group] error: ", e);
        }
    }

    private void single(MessageFrame frame) {
        try {
            Channel channel = channelManager.get(frame.getDestination());
            ResponseEntity response = new ResponseEntity(frame.getDestination(), frame.getMessage(), frame.getData());
            channel.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(response)));
        } catch (JsonProcessingException e) {
            log.error("[WebSocketDispatcher:single] error: ", e);
        }
    }
}
