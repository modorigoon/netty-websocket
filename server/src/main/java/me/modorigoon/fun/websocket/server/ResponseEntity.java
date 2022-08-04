package me.modorigoon.fun.websocket.server;

import java.time.LocalDateTime;


/**
 * @author modorigoon
 * @since 2018.
 */
public class ResponseEntity {

    private ResponseStatus status;
    private String identifier;
    private String message;
    private Object body;
    private LocalDateTime time;

    public ResponseEntity() {
    }

    public ResponseEntity(ResponseStatus status, String identifier, String message, Object body) {
        this.status = status;
        this.identifier = identifier;
        this.message = message;
        this.body = body;
        this.time = LocalDateTime.now();
    }

    public enum ResponseStatus {
        OK, ERROR, BAD_REQUEST
    }

    public ResponseEntity(String identifier, String message, Object body) {
        this(ResponseStatus.OK, identifier, message, body);
    }

    public ResponseEntity(ResponseStatus status, String message) {
        this(status, null, message, null);
    }

    public ResponseEntity(ResponseStatus status) {
        this(status, null, null, null);
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getMessage() {
        return message;
    }

    public Object getBody() {
        return body;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
