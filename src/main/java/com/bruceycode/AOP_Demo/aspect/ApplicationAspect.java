package com.bruceycode.AOP_Demo.aspect;

import com.bruceycode.AOP_Demo.model.ErrorResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Aspect
@Component
public class ApplicationAspect {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationAspect.class);

    // Log and measure execution time for controller and service methods
    @Around("execution(* com.bruceycode.AOP_Demo.controller..*.*(..)) || " +
            "execution(* com.bruceycode.AOP_Demo.service..*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        // Log method entry
        logger.debug("Entering method: {} with arguments: {}", methodName, args);

        long startTime = System.currentTimeMillis();
        try {
            // Proceed with method execution
            Object result = joinPoint.proceed();

            // Log method exit and execution time
            long executionTime = System.currentTimeMillis() - startTime;
            logger.debug("Exiting method: {} with result: {} (Execution time: {} ms)",
                    methodName, result, executionTime);

            return result;
        } catch (Throwable t) {
            // Log exception
            logger.error("Exception in method: {} - {}", methodName, t.getMessage());
            throw t;
        }
    }


    @AfterThrowing(
            pointcut = "execution(* com.bruceycode.AOP_Demo.controller..*.*(..))",
            throwing = "ex"
    )
    public ResponseEntity<ErrorResponse> handleControllerExceptions(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().toShortString();
        logger.error("Error in method: {} - {}", methodName, ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorcode("INTERNAL_SERVER_ERROR");
        errorResponse.setMessage("An error occurred while processing the request");
        errorResponse.setDetails(ex.getMessage());

        if (ex instanceof IllegalArgumentException) {
            errorResponse.setErrorcode("BAD_REQUEST");
            errorResponse.setMessage("Invalid input provided");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof NoSuchElementException) {
            errorResponse.setErrorcode("NOT_FOUND");
            errorResponse.setMessage("Resource not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
