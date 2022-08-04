package me.modorigoon.fun.websocket.server.netty;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author modorigoon
 * @since 2018.
 */
@Component
public class ChannelManager {

    private static final String CHANNEL_ID_ATTRIBUTE_KEY = "CHANNEL_ID:";

    private final Map<String, Channel> channels;

    public ChannelManager() {
        this.channels = new ConcurrentHashMap<>();
    }

    public Channel add(String id, Channel channel) {
        setChannelIdAttributeKey(channel, id);
        this.channels.put(id, channel);
        return channel;
    }

    public Channel get(String id) {
        return this.channels.get(id);
    }

    public Channel remove(String id) {
        return this.channels.remove(id);
    }

    public Channel remove(Channel channel) {
        String id = getChannelIdAttributeKey(channel);
        if (StringUtils.isNotBlank(id)) {
            return channels.remove(id);
        }
        return null;
    }

    AttributeKey<String> channelAttributeKey() {
        return !AttributeKey.exists(CHANNEL_ID_ATTRIBUTE_KEY) ?
                AttributeKey.newInstance(CHANNEL_ID_ATTRIBUTE_KEY) : AttributeKey.valueOf(CHANNEL_ID_ATTRIBUTE_KEY);
    }

    private void setChannelIdAttributeKey(Channel channel, String id) {
        if (channel != null && id != null) {
            channel.attr(channelAttributeKey()).set(id);
        }
    }

    private String getChannelIdAttributeKey(Channel channel) {
        return channel.attr(channelAttributeKey()).get();
    }
}
