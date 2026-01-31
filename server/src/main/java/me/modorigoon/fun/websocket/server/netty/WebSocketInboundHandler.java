package me.modorigoon.fun.websocket.server.netty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import me.modorigoon.fun.websocket.server.RequestEntity;
import me.modorigoon.fun.websocket.server.ResponseEntity;
import me.modorigoon.fun.websocket.server.invoker.WebSocketAdviceInvoker;
import me.modorigoon.fun.websocket.server.invoker.WebSocketControllerInvoker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;


/**
 * @author modorigoon
 * @since 2018.
 */
@Component("webSocketInboundHandler")
@ChannelHandler.Sharable
public class WebSocketInboundHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final WebSocketControllerInvoker webSocketControllerInvoker;
    private final WebSocketAdviceInvoker webSocketAdviceInvoker;
    private final ChannelGroupManager channelGroupManager;
    private final ChannelManager channelManager;
    private final ObjectMapper objectMapper;

    Logger log = LoggerFactory.getLogger(WebSocketInboundHandler.class);

    public WebSocketInboundHandler(WebSocketControllerInvoker webSocketControllerInvoker,
                                   WebSocketAdviceInvoker webSocketAdviceInvoker,
                                   ChannelGroupManager channelGroupManager,
                                   ChannelManager channelManager,
                                   ObjectMapper objectMapper) {
        this.webSocketControllerInvoker = webSocketControllerInvoker;
        this.webSocketAdviceInvoker = webSocketAdviceInvoker;
        this.channelGroupManager = channelGroupManager;
        this.channelManager = channelManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (Objects.requireNonNull(e.state()) == IdleState.ALL_IDLE) {
                ctx.close();
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        channelManager.remove(ctx.channel());
        channelGroupManager.removeChannelInAllGroups(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        CompletableFuture<ResponseEntity> future = new CompletableFuture<>();
        RequestEntity req = objectMapper.readValue(frame.text(), RequestEntity.class);
        if (StringUtils.isEmpty(req.getMapper())) {
            future.completeExceptionally(new WebSocketInboundException("Invalid request mapper name."));
        } else {
            webSocketControllerInvoker.invoke(req, ctx, future);
        }
        future.exceptionally((Throwable t) -> {
            try {
                Object except = webSocketAdviceInvoker.invoke(t);
                sendResponse(ctx.channel(), (ResponseEntity) except);
            } catch (InvocationTargetException | IllegalAccessException e) {
                log.error("[WebSocketInboundHandler:channelRead0] error: ", e);
            }
            return null;
        });
        future.thenAccept(response -> sendResponse(ctx.channel(), response));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        sendResponse(ctx.channel(), new ResponseEntity(ResponseEntity.ResponseStatus.ERROR, "server error."));
    }

    private void sendResponse(Channel channel, ResponseEntity response) {
        if (channel == null || !channel.isActive()) {
            return;
        }
        try {
            channel.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(response)));
        } catch (JsonProcessingException e) {
            log.error("[WebSocketInboundHandler:sendResponse] error: ", e);
        }
    }
}
