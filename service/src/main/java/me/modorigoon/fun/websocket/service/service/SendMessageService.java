package me.modorigoon.fun.websocket.service.service;

import me.modorigoon.fun.websocket.server.WebSocketTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


/**
 * @author modorigoon
 * @since 2018.
 */
@Service
public class SendMessageService {

    private final WebSocketTemplate webSocketTemplate;

    public SendMessageService(WebSocketTemplate webSocketTemplate) {
        this.webSocketTemplate = webSocketTemplate;
    }

    public void sendToUser(String token, String message, Object data) {
        if (StringUtils.isEmpty(token)) {
            throw new IllegalArgumentException("User token can not be an empty value.");
        }
        webSocketTemplate.send(token, message, data);
    }

    public void sendToGroup(String groupName, String message, Object data) {
        if (StringUtils.isEmpty(groupName)) {
            throw new IllegalArgumentException("Group name can not be an empty value.");
        }
        webSocketTemplate.sendToGroup(groupName, message, data);
    }
}
