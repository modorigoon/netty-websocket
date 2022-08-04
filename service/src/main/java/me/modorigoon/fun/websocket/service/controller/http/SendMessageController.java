package me.modorigoon.fun.websocket.service.controller.http;

import me.modorigoon.fun.websocket.service.dto.SendMessage;
import me.modorigoon.fun.websocket.service.service.SendMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * @author modorigoon
 * @since 2018.
 */
@RestController
@RequestMapping("/send")
public class SendMessageController {

    private final SendMessageService sendMessageService;

    public SendMessageController(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public SendMessage.Response send(@RequestBody SendMessage.Request req) {
        sendMessageService.sendToUser(req.getDestination(), req.getMessage(), req.getData());
        return new SendMessage.Response(req.getDestination(), LocalDateTime.now());
    }

    @PostMapping("/group")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public SendMessage.Response sendToGroup(@RequestBody SendMessage.Request req) {
        sendMessageService.sendToGroup(req.getDestination(), req.getMessage(), req.getData());
        return new SendMessage.Response(req.getDestination(), LocalDateTime.now());
    }
}
