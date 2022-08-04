package me.modorigoon.fun.websocket.server.pubsub;


/**
 * @author modorigoon
 * @since 2018.
 */
public class MessageFrame {

    private ChannelType channelType;
    private String destination;
    private String message;
    private Object data;

    public MessageFrame() {
    }

    public MessageFrame(ChannelType channelType, String destination, String message, Object data) {
        this.channelType = channelType;
        this.destination = destination;
        this.message = message;
        this.data = data;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public String getDestination() {
        return destination;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public enum ChannelType {
        GROUP, SINGLE
    }
}
