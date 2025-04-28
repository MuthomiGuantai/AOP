package com.bruceycode.AOP_Demo.aspect;

import com.bruceycode.AOP_Demo.entity.Task;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;

@Aspect
@Component
public class ValidationAspect {

    private static final Logger logger = LoggerFactory.getLogger(ValidationAspect.class);

    @Before("execution(* com.bruceycode.AOP_Demo.controller.TaskController.createTask(..)) || " +
            "execution(* com.bruceycode.AOP_Demo.controller.TaskController.updateTask(..))")
    public void enforceCustomValidation(org.aspectj.lang.JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof Task task) {
                if (task.getDescription() != null && !task.getDescription().isEmpty() && task.getDescription().length() < 10) {
                    throw new IllegalArgumentException("Description must be at least 10 characters long if provided");
                }
            }
        }
    }

    @AfterThrowing(
            pointcut = "execution(* com.bruceycode.AOP_Demo.controller..*.*(..))",
            throwing = "ex"
    )
    public void logValidationErrors(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        logger.warn("Validation errors in request: {}", errors);
    }
}