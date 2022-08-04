package me.modorigoon.fun.websocket.service;

import me.modorigoon.fun.websocket.server.netty.NettyServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author modorigoon
 * @since 2018.
 */
@SpringBootApplication
@ComponentScan(basePackages = {
        "me.modorigoon.fun.websocket.server",
        "me.modorigoon.fun.websocket.service"
})
public class ServiceApplication {

    private final NettyServer nettyServer;

    public ServiceApplication(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> nettyServer.start();
    }
}
