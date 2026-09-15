package com.tyss.EMS.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String clientIp = request.getRemoteAddr();

        logger.info(
                "REQUEST | Method={} | URI={} | ClientIP={}",
                method,
                uri,
                clientIp
        );

        try {

            // Continue request to controller/security filters
            filterChain.doFilter(request, response);

        } finally {

            long executionTime =
                    System.currentTimeMillis() - startTime;

            int status = response.getStatus();

            logger.info(
                    "RESPONSE | Method={} | URI={} | Status={} | Time={}ms",
                    method,
                    uri,
                    status,
                    executionTime
            );
        }
    }
}