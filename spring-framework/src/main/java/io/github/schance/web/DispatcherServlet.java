package io.github.schance.web;

import com.alibaba.fastjson2.JSONObject;
import io.github.schance.BeanPostProcessor;
import io.github.schance.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DispatcherServlet extends HttpServlet implements BeanPostProcessor {

    Map<String, WebHandler> handlerMap = new HashMap<>();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebHandler handler = findHandler(req);
        if (handler == null) {
            resp.setContentType("text/html");
            resp.getWriter().write("<h1>Error Request not found handler</h1> <br> ");
            return;
        }
        try {
            Object controllerBean = handler.getControllerBean();
            Object[] args = resolveArgs(req, handler.getMethod());
            Object result = handler.getMethod().invoke(controllerBean, args);
            switch (handler.getResultType()) {
                case HTML -> {
                    resp.setContentType("text/html;charset=UTF-8");
                    resp.getWriter().write(result.toString());
                }
                case JSON -> {
                    resp.setContentType("application/json;charset=UTF-8");
                    resp.getWriter().write(JSONObject.toJSONString(result));
                }
                case LOCAL -> {
                    ModelAndView mv = (ModelAndView) result;
                    String view = mv.getView();
                    InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(view);
                    try (resourceAsStream) {
                        String html = new String(resourceAsStream.readAllBytes());
                        html = renderTemplate(html, mv.getContext());
                        resp.setContentType("text/html;charset=UTF-8");
                        resp.getWriter().write(html);
                    }

                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // {{}}
    private static final Pattern PATTERN = Pattern.compile("\\{\\{(.*?)}}");

    private String renderTemplate(String template, Map<String, String> context) {
        Matcher matcher = PATTERN.matcher(template);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = context.getOrDefault(key, "");
            matcher.appendReplacement(result, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private Object[] resolveArgs(HttpServletRequest req, Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Param param = parameter.getAnnotation(Param.class);
            String value = null;
            if (param != null) {
                value = req.getParameter(param.value());
            } else {
                value = req.getParameter(parameter.getName());
            }
            Class<?> parameterType = parameter.getType();
            if (String.class.isAssignableFrom(parameterType)) {
                args[i] = value;
            } else if (Integer.class.isAssignableFrom(parameterType)) {
                args[i] = Integer.parseInt(value);
            } else {
                args[i] = null;
            }
        }
        return args;
    }

    private WebHandler findHandler(HttpServletRequest req) {
        return handlerMap.get(req.getRequestURI());
    }

    @Override
    public Object afterInitializeBean(Object bean, String beanName) {
        if (!bean.getClass().isAnnotationPresent(Controller.class)) {
            return bean;
        }
        RequestMapping classRM = bean.getClass().getAnnotation(RequestMapping.class);
        String classUrl = classRM != null ? classRM.value() : "";
        Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .forEach(m -> {
                    RequestMapping methodRM = m.getAnnotation(RequestMapping.class);
                    String key = classUrl.concat(methodRM.value());
                    WebHandler webHandler = new WebHandler(bean, m);
                    if (handlerMap.put(key, webHandler) != null) {
                        throw new RuntimeException("Duplicate request handler " + key);
                    }
                });
        return bean;
    }
}
