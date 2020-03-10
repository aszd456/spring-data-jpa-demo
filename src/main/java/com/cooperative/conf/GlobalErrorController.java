package com.cooperative.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class GlobalErrorController extends AbstractErrorController {
    private static final String ERROR_PATH = "/error";

    @Autowired
    ObjectMapper objectMapper;

    public GlobalErrorController() {
        super(new DefaultErrorAttributes());
    }


    @RequestMapping(ERROR_PATH)
    public ModelAndView getErrorPath(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(request, false));
        Throwable cause = getCause(request);
        int status = (int) model.get("status");
        String message = (String) model.get("message");
        String errorMessage = getErrorMessage(cause);

        log.info(status + "," + message, cause);
        response.setStatus(status);
        if (!isJsonRequest(request)) {
            ModelAndView view = new ModelAndView("/error.html");
            view.addAllObjects(model);
            view.addObject("errorMessage", errorMessage);
            view.addObject("status", status);
            view.addObject("cause", cause);
            return view;
        } else {
            Map error = new HashMap();
            error.put("success", false);
            error.put("errorMessage", errorMessage);
            error.put("message", message);
            writeJson(response, error);
            return null;
        }

    }

    protected void writeJson(HttpServletResponse response, Map error) {
        response.setContentType("application/json;charset=utf-8");
        try {
            response.getWriter().write(objectMapper.writeValueAsString(error));
        } catch (IOException e) {
            // ignore
        }
    }


    protected Throwable getCause(HttpServletRequest request) {
        Throwable error = (Throwable) request.getAttribute("javax.servlet.error.exception");
        if (null != error) {
            while (error instanceof ServletException && error.getCause() != null) {
                error = ((ServletException) error).getCause();
            }
        }
        return error;
    }

    protected String getErrorMessage(Throwable ex) {
        return "服务器错误，请联系管理员";
    }

    protected boolean isJsonRequest(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        if (requestUri.endsWith(".json")) {
            return true;
        } else {
            return (request.getHeader("accept").contains("application/json") || (request.getHeader("X-Requested-With") != null
                    && request.getHeader("X-Requested-With").contains("XMLHttpRequest")));
        }
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
