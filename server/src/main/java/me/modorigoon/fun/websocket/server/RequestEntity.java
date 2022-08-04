package me.modorigoon.fun.websocket.server;


/**
 * @author modorigoon
 * @since 2018.
 */
public class RequestEntity {

    private String mapper;
    private Object body;

    public RequestEntity() {
    }

    public RequestEntity(String mapper, Object body) {
        this.mapper = mapper;
        this.body = body;
    }

    public String getMapper() {
        return mapper;
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
