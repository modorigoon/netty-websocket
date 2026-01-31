package me.modorigoon.fun.websocket.service.controller.websocket;

import io.netty.channel.ChannelHandlerContext;
import me.modorigoon.fun.websocket.server.ResponseEntity;
import me.modorigoon.fun.websocket.server.annotation.WebSocketController;
import me.modorigoon.fun.websocket.server.annotation.WebSocketRequestMapping;
import me.modorigoon.fun.websocket.service.dto.Group;
import me.modorigoon.fun.websocket.service.exception.GroupProcessingException;
import me.modorigoon.fun.websocket.service.service.GroupService;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;


/**
 * @author modorigoon
 * @since 2018.
 */
@WebSocketController
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @WebSocketRequestMapping(value = "join")
    public void join(Group.Request req, ChannelHandlerContext ctx, CompletableFuture<ResponseEntity> future) {
        boolean joinSuccessful = groupService.join(req.getGroupName(), ctx.channel());
        if (!joinSuccessful) {
            future.completeExceptionally(new GroupProcessingException("Join to group failure."));
            return;
        }
        Group.Response response = new Group.Response(LocalDateTime.now(), "Hi!");
        future.complete(new ResponseEntity(req.getGroupName(), "Done.", response));
    }

    @WebSocketRequestMapping(value = "leave")
    public void leave(Group.Request req, ChannelHandlerContext ctx, CompletableFuture<ResponseEntity> future) {
        boolean leaveSuccessful = groupService.leave(req.getGroupName(), ctx.channel());
        if (!leaveSuccessful) {
            future.completeExceptionally(new GroupProcessingException("Leave group failure."));
            return;
        }
        Group.Response response = new Group.Response(LocalDateTime.now(), "Bye!");
        future.complete(new ResponseEntity(req.getGroupName(), "Done.", response));
    }
}
