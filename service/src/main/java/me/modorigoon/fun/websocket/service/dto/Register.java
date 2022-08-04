package me.modorigoon.fun.websocket.service.dto;

import java.time.LocalDateTime;


/**
 * @author modorigoon
 * @since 2018.
 */
public class Register {

    private Register() {}

    public static class Request {

        private String token;
        private String agent;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }
    }

    public static class Response {

        private final LocalDateTime registeredAt;
        private final String message;

        public Response(LocalDateTime registeredAt, String message) {
            this.registeredAt = registeredAt;
            this.message = message;
        }

        public LocalDateTime getRegisteredAt() {
            return registeredAt;
        }

        public String getMessage() {
            return message;
        }
    }
}
