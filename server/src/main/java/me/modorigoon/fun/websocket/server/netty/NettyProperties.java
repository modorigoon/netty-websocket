package me.modorigoon.fun.websocket.server.netty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;


/**
 * @author modorigoon
 * @since 2018.
 */
@Configuration
@ConfigurationProperties(prefix = "websocket.netty")
public class NettyProperties {

    private String socketPath;
    private Integer maxContentLength;
    private Integer port;
    private Integer bossThread;
    private Integer workerThread;


    public void setSocketPath(String socketPath) {
        this.socketPath = socketPath;
    }

    public void setMaxContentLength(Integer maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setBossThread(Integer bossThread) {
        this.bossThread = bossThread;
    }

    public void setWorkerThread(Integer workerThread) {
        this.workerThread = workerThread;
    }

    public String getSocketPath() {
        return Objects.requireNonNullElse(socketPath, "");
    }

    public Integer getMaxContentLength() {
        return Objects.requireNonNullElse(maxContentLength, 65536);
    }

    public Integer getPort() {
        return Objects.requireNonNullElse(port, 8090);
    }

    public Integer getBossThread() {
        return Objects.requireNonNullElse(bossThread, 1);
    }

    public Integer getWorkerThread() {
        return Objects.requireNonNullElse(workerThread, 2);
    }
}
