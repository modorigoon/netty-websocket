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
public class GroupService {

    private final WebSocketTemplate webSocketTemplate;

    public GroupService(WebSocketTemplate webSocketTemplate) {
        this.webSocketTemplate = webSocketTemplate;
    }

    public boolean join(String groupName, Channel channel) {
        if (StringUtils.isEmpty(groupName)) {
            throw new IllegalArgumentException("Group name can not be an empty value.");
        }
        if (channel == null) {
            throw new IllegalArgumentException("Channel can not be a null.");
        }
        return webSocketTemplate.joinGroup(groupName, channel);
    }

    public boolean leave(String groupName, Channel channel) {
        if (StringUtils.isEmpty(groupName)) {
            throw new IllegalArgumentException("Group name can not be an empty value.");
        }
        if (channel == null) {
            throw new IllegalArgumentException("Channel can not be a null.");
        }
        return webSocketTemplate.leaveGroup(groupName, channel);
    }
}
