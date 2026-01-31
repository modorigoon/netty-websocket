package me.modorigoon.fun.websocket.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;


/**
 * @author modorigoon
 * @since 2018.
 */
@Component
public class NettyServer {

    private final ServerBootstrap serverBootstrap;
    private final InetSocketAddress nettyTcpPort;
    private Channel channel;

    Logger log = LoggerFactory.getLogger(NettyServer.class);

    public NettyServer(ServerBootstrap serverBootstrap, InetSocketAddress nettyTcpPort) {
        this.serverBootstrap = serverBootstrap;
        this.nettyTcpPort = nettyTcpPort;
    }

    public void start() throws InterruptedException {
        log.info("[NettyServer:start] start server. (bind: {})", nettyTcpPort);
        channel = serverBootstrap.bind(nettyTcpPort).sync().channel().closeFuture().sync().channel();
    }

    @PreDestroy
    public void stop() {
        log.info("[NettyServer:stop] stop server.");
        if (channel != null) {
            channel.close();
            if (channel.parent() != null) {
                channel.parent().close();
            }
        }
    }
}
