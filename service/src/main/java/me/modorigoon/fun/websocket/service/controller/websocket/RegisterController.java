package me.modorigoon.fun.websocket.service.controller.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import me.modorigoon.fun.websocket.server.ResponseEntity;
import me.modorigoon.fun.websocket.server.annotation.WebSocketController;
import me.modorigoon.fun.websocket.server.annotation.WebSocketRequestMapping;
import me.modorigoon.fun.websocket.service.dto.Register;
import me.modorigoon.fun.websocket.service.exception.RegisterProcessingException;
import me.modorigoon.fun.websocket.service.service.RegisterService;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;


/**
 * @author modorigoon
 * @since 2018.
 */
@WebSocketController
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @WebSocketRequestMapping(value = "register")
    public void register(Register.Request req, ChannelHandlerContext ctx, CompletableFuture<ResponseEntity> future) {
        Channel channel = registerService.register(req.getToken(), ctx.channel());
        if (channel == null) {
            future.completeExceptionally(new RegisterProcessingException("Oops! user register failure."));
            return;
        }
        Register.Response response = new Register.Response(LocalDateTime.now(), "Hi! Good 2 see U!");
        future.complete(new ResponseEntity(req.getToken(), "Agent: " + req.getAgent(), response));
    }

    @WebSocketRequestMapping(value = "unregister")
    public void unregister(ChannelHandlerContext ctx, CompletableFuture<ResponseEntity> future) {
        registerService.unregister(ctx.channel());
        future.complete(new ResponseEntity(ResponseEntity.ResponseStatus.OK));
    }
}
