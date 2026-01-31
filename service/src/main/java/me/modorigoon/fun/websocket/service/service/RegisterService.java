package me.modorigoon.fun.websocket.service.service;

import io.netty.channel.Channel;
import me.modorigoon.fun.websocket.server.WebSocketTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


/**
 * @author modorigoon
 * @since 2018.
 */
@Service
public class RegisterService {

    private final WebSocketTemplate webSocketTemplate;

    public RegisterService(WebSocketTemplate webSocketTemplate) {
        this.webSocketTemplate = webSocketTemplate;
    }

    public Channel register(String token, Channel channel) {
        if (StringUtils.isEmpty(token)) {
            throw new IllegalArgumentException("User token can not be an empty value.");
        }
        if (channel == null) {
            throw new IllegalArgumentException("User channel can not be a null.");
        }
        if (webSocketTemplate.isRegistered(token)) {
            return null;
        }
        return webSocketTemplate.join(token, channel);
    }

    public Channel unregister(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("User channel can not be a null.");
        }
        return webSocketTemplate.leave(channel);
    }
}
