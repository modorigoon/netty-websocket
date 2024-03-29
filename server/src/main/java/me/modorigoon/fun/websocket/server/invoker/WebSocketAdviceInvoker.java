package me.modorigoon.fun.websocket.server.invoker;

import me.modorigoon.fun.websocket.server.annotation.WebSocketControllerAdvice;
import me.modorigoon.fun.websocket.server.annotation.WebSocketExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @author modorigoon
 * @since 2018.
 */
@Component
public class WebSocketAdviceInvoker {

    private final List<Object> adviceBeans;

    public WebSocketAdviceInvoker(ApplicationContext context) {
        adviceBeans = new CopyOnWriteArrayList<>(
                context.getBeansWithAnnotation(WebSocketControllerAdvice.class).values());
    }

    private Method getAdvisor(Object bean, Throwable throwable) {
        if (bean == null || throwable == null) {
            return null;
        }
        for (Method method : bean.getClass().getMethods()) {
            WebSocketExceptionHandler handler = method.getAnnotation(WebSocketExceptionHandler.class);
            Class<? extends Throwable>[] throwableClasses = handler.throwables();
            for (Class<? extends Throwable> cls : throwableClasses) {
                if (StringUtils.equals(throwable.getClass().getName(), cls.getName())) {
                    return method;
                }
            }
        }
        return null;
    }

    public Object invoke(Throwable throwable) throws InvocationTargetException, IllegalAccessException {
        if (throwable == null) {
            return null;
        }
        for (Object bean : adviceBeans) {
            Method advisor = getAdvisor(bean, throwable);
            if (advisor != null) {
                return advisor.invoke(bean, throwable);
            }
        }
        return null;
    }
}
