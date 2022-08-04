package me.modorigoon.fun.websocket.service.dto;

import java.time.LocalDateTime;


/**
 * @author modorigoon
 * @since 2018.
 */
public class Group {

    private Group() {}

    public static class Request {

        private String groupName;

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }
    }

    public static class Response {

        private final LocalDateTime joinedAt;
        private final String message;

        public Response(LocalDateTime joinedAt, String message) {
            this.joinedAt = joinedAt;
            this.message = message;
        }

        public LocalDateTime getJoinedAt() {
            return joinedAt;
        }

        public String getMessage() {
            return message;
        }
    }
}
