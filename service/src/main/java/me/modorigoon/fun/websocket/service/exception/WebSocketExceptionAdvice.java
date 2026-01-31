package me.modorigoon.fun.websocket.service.exception;

import me.modorigoon.fun.websocket.server.ResponseEntity;
import me.modorigoon.fun.websocket.server.annotation.WebSocketControllerAdvice;
import me.modorigoon.fun.websocket.server.annotation.WebSocketExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author modorigoon
 * @since 2018.
 */
@WebSocketControllerAdvice
public class WebSocketExceptionAdvice {

    Logger log = LoggerFactory.getLogger(WebSocketExceptionAdvice.class);

    @WebSocketExceptionHandler(throwables = { GroupProcessingException.class })
    public ResponseEntity groupProcessingExceptionHandler(GroupProcessingException e) {
        log.error("[GroupProcessingException] error: ", e);
        return new ResponseEntity(ResponseEntity.ResponseStatus.ERROR, e.getMessage());
    }

    @WebSocketExceptionHandler(throwables = { RegisterProcessingException.class })
    public ResponseEntity registerProcessingExceptionHandler(RegisterProcessingException e) {
        log.error("[RegisterProcessingException] error: ", e);
        return new ResponseEntity(ResponseEntity.ResponseStatus.ERROR, e.getMessage());
    }

    @WebSocketExceptionHandler(throwables = { IllegalArgumentException.class })
    public ResponseEntity illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.warn("[IllegalArgumentException] error: {}", e.getMessage());
        return new ResponseEntity(ResponseEntity.ResponseStatus.BAD_REQUEST, e.getMessage());
    }
}
