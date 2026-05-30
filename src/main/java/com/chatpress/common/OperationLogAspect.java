package com.chatpress.common;

import com.chatpress.common.annotation.LogOperation;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class OperationLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);

    private final OperationLogRepository operationLogRepository;

    public OperationLogAspect(OperationLogRepository operationLogRepository) {
        this.operationLogRepository = operationLogRepository;
    }

    @Around("@annotation(com.chatpress.common.annotation.LogOperation)")
    public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            LogOperation annotation = method.getAnnotation(LogOperation.class);

            String username = SecurityContextHolder.getContext().getAuthentication() != null
                    ? SecurityContextHolder.getContext().getAuthentication().getName()
                    : "anonymous";

            String target = buildTarget(joinPoint.getArgs(), signature.getParameterNames());

            com.chatpress.common.OperationLog logEntry = new com.chatpress.common.OperationLog(
                    username, annotation.value(), target, duration
            );
            try {
                operationLogRepository.save(logEntry);
            } catch (Exception e) {
                log.error("Failed to save operation log: {}", e.getMessage(), e);
            }
        }
    }

    private String buildTarget(Object[] args, String[] paramNames) {
        List<String> parts = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof HttpServletRequest || arg instanceof MultipartFile) {
                continue;
            }
            String name = paramNames != null && i < paramNames.length ? paramNames[i] : "arg" + i;
            String value = arg != null ? arg.toString() : "null";
            if (value.length() > 100) {
                value = value.substring(0, 100) + "...";
            }
            parts.add(name + "=" + value);
        }
        return String.join(", ", parts);
    }
}
