package me.modorigoon.fun.websocket.server.netty;


/**
 * @author modorigoon
 * @since 2018.
 */
public class AlreadyChannelGroupException extends Exception {

    private static final long serialVersionUID = 569521806295270099L;

    public AlreadyChannelGroupException(String message) {
        super(message);
    }
}
