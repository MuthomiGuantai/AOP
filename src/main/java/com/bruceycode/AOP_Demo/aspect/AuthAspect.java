package com.bruceycode.AOP_Demo.aspect;

import com.bruceycode.AOP_Demo.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Api for user authentication")
public class AuthAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuthAspect.class);

    private final JwtUtil jwtUtil;

    @Before("execution(* com.bruceycode.AOP_Demo.controller.TaskController.deleteTask(..))")
    public void enforceAdminRole() {
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        String role = jwtUtil.extractRole(token);
        logger.info("Checking role for deleteTask: {}", role);
        if (!"ADMIN".equals(role)) {
            throw new SecurityException("Only ADMIN role can delete tasks");
        }
    }
}
