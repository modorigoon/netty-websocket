package me.modorigoon.fun.websocket.server.annotation;

import java.lang.annotation.*;


/**
 * @author modorigoon
 * @since 2018.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocketExceptionHandler {

    Class<? extends Throwable>[] throwables() default {};
}
