package me.modorigoon.fun.websocket.server.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import org.springframework.stereotype.Component;


/**
 * @author modorigoon
 * @since 2018.
 */
@Component
public class NettyChannelInitializer extends ChannelInitializer<Channel> {

    private final WebSocketInboundHandler webSocketInboundHandler;
    private final NettyProperties nettyProperties;

    public NettyChannelInitializer(WebSocketInboundHandler webSocketInboundHandler,
                                   NettyProperties nettyProperties) {
        this.webSocketInboundHandler = webSocketInboundHandler;
        this.nettyProperties = nettyProperties;
    }

    @Override
    protected void initChannel(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline
                .addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(nettyProperties.getMaxContentLength()))
                .addLast(new WebSocketServerCompressionHandler())
                .addLast(new WebSocketServerProtocolHandler(nettyProperties.getSocketPath(), null, true))
                .addLast(webSocketInboundHandler);
    }
}
