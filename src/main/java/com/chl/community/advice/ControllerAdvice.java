package com.chl.community.advice;

import com.chl.community.utils.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@org.springframework.web.bind.annotation.ControllerAdvice(annotations = Controller.class)
public class ControllerAdvice {
    private final static Logger log = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public void handlerException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error("服务器发生异常 : " + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()){
            log.error(element.toString());
        }
        String header = request.getHeader("x-requested-with");
        if ("XMLhttpRequest".equals(header)){
            response.setContentType("application/plain:charset=utf-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(CommunityUtil.getJSONString(1, "服务器异常"));
        }else response.sendRedirect(request.getContextPath() + "/error");
    }
}
