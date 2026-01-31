package me.modorigoon.fun.websocket.server.invoker;

import io.netty.channel.ChannelHandlerContext;
import me.modorigoon.fun.websocket.server.RequestEntity;
import me.modorigoon.fun.websocket.server.ResponseEntity;
import me.modorigoon.fun.websocket.server.annotation.WebSocketController;
import me.modorigoon.fun.websocket.server.annotation.WebSocketRequestMapping;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @author modorigoon
 * @since 2018.
 */
@Component
public class WebSocketControllerInvoker {

    private final List<Object> controllerBeans;
    private final ModelMapper modelMapper;

    public WebSocketControllerInvoker(ApplicationContext context, ModelMapper modelMapper) {
        this.controllerBeans = new CopyOnWriteArrayList<>(
                context.getBeansWithAnnotation(WebSocketController.class).values());
        this.modelMapper = modelMapper;
    }

    private Method findMethod(Object bean, String name) {
        return Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(m -> {
                    WebSocketRequestMapping mapping = m.getAnnotation(WebSocketRequestMapping.class);
                    return mapping != null && StringUtils.equalsIgnoreCase(name, mapping.value());
                })
                .findFirst().orElse(null);
    }

    private Map.Entry<Object, Method> findController(String mapper) {
        for (Object bean : controllerBeans) {
            Method method = findMethod(bean, mapper);
            if (method != null) {
                return new AbstractMap.SimpleEntry<>(bean, method);
            }
        }
        return null;
    }

    private boolean hasRequestBody(Method method) {
        Parameter[] parameters = method.getParameters();
        return parameters.length > 0 &&
                !ChannelHandlerContext.class.isAssignableFrom(parameters[0].getType());
    }

    private void invokeMethod(Object bean, Method method, RequestEntity req,
                              ChannelHandlerContext ctx,
                              CompletableFuture<ResponseEntity> future) throws ReflectiveOperationException {
        if (hasRequestBody(method)) {
            Object arg = modelMapper.map(req.getBody(), method.getParameters()[0].getType());
            if (arg == null) {
                throw new IllegalArgumentException("Request body parameter cannot be null.");
            }
            method.invoke(bean, arg, ctx, future);
        } else {
            method.invoke(bean, ctx, future);
        }
    }

    public void invoke(RequestEntity req, ChannelHandlerContext ctx,
                       CompletableFuture<ResponseEntity> future) {
        Map.Entry<Object, Method> controller = findController(req.getMapper());
        if (controller == null) {
            future.completeExceptionally(new ControllerInvokeException("Websocket controller not found."));
            return;
        }

        try {
            invokeMethod(controller.getKey(), controller.getValue(), req, ctx, future);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
    }
}
