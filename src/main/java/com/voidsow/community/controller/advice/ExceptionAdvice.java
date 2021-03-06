package com.voidsow.community.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidsow.community.dto.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {
    private static Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);
    ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器内部异常：" + e.getMessage());
        for (var element : e.getStackTrace())
            logger.error(element.toString());

        //处理异步资源请求
        if (request.getHeader("Content-Type").equals("application/json")) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(new Result(500, "服务器异常", null)));
        } else
            response.sendRedirect(request.getContextPath() + "/error");
    }
}
