package com.bruceycode.AOP_Demo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class QueryLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(QueryLoggingAspect.class);

    @Before("execution(* com.bruceycode.AOP_Demo.controller.TaskController.getAllTasks(..))")
    public void logQueryParameters() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String queryString = request.getQueryString();
        logger.info("Query parameters for GET /api/tasks: {}", queryString != null ? queryString : "none");
    }
}
