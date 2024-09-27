package com.application.apigateway.config.logger;

import com.application.banking_system_monolithic.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggerAspect {

    @Around(value = "within(com.application.banking_system_monolithic.service..*) && within(com.application.banking_system_monolithic.controller..*)")
    public Object log(final ProceedingJoinPoint joinPoint) throws Throwable {
        final String methodName = joinPoint.getSignature().getName();
        final String className = joinPoint.getTarget().getClass().getSimpleName();
        final Object[] args = joinPoint.getArgs();

        if (args.length > 0) {
            log.info("Invoked.. {}::{}() and args={}", className, methodName, Utility.getJsonString(args));
        } else {
            log.info("Invoked.. {}::{}()", className, methodName);
        }
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long timeMillis = System.currentTimeMillis() - start;
        log.info("Complete[{}ms].. {}::{}() and Result={}", timeMillis, className, methodName, Utility.getJsonString(result));
        return result;
    }
}
