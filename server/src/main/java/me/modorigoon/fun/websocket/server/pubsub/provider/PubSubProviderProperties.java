package me.modorigoon.fun.websocket.server.pubsub.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * @author modorigoon
 * @since 2018.
 */
@Configuration
@ConfigurationProperties(prefix = "websocket.pubsub")
public class PubSubProviderProperties {

    private String provider;
    private String host;
    private Integer port;
    private String channel;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
