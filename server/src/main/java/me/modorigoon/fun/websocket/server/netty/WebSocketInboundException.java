package me.modorigoon.fun.websocket.server.netty;


/**
 * @author modorigoon
 * @since 2018.
 */
public class WebSocketInboundException extends Exception {

    private static final long serialVersionUID = 3025904853730721668L;

    public WebSocketInboundException(String message) {
        super(message);
    }
}
