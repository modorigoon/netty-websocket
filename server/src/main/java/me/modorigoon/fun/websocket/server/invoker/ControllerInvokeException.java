package me.modorigoon.fun.websocket.server.invoker;


/**
 * @author modorigoon
 * @since 2018.
 */
public class ControllerInvokeException  extends Exception {

    private static final long serialVersionUID = 4165304767110494088L;

    public ControllerInvokeException(String message) {
        super(message);
    }
}
