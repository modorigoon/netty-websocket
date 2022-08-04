package me.modorigoon.fun.websocket.service.dto;

import java.time.LocalDateTime;


/**
 * @author modorigoon
 * @since 2018.
 */
public class SendMessage {

    private SendMessage() {}

    public static class Request {

        private String destination;
        private String message;
        private Object data;

        public String getDestination() {
            return destination;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }

    public static class Response {

        private final String destination;
        private final LocalDateTime sentAt;

        public Response(String destination, LocalDateTime sentAt) {
            this.destination = destination;
            this.sentAt = sentAt;
        }

        public String getDestination() {
            return destination;
        }

        public LocalDateTime getSentAt() {
            return sentAt;
        }
    }
}
