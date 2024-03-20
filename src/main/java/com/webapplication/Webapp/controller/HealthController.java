package com.webapplication.Webapp.controller;

import com.webapplication.Webapp.service.HealthCheckService;

import org.springframework.http.HttpHeaders;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController

public class HealthController {
    @Autowired
    private HealthCheckService healthCheckService;

    private static final Logger log = LogManager.getLogger(HealthController.class);

    @GetMapping(value = "/healthz")
    public ResponseEntity<Void> healthCheck(HttpServletRequest request, String requestBody, String responseBody, String operation, String sourceLocation, String spanId, String trace, String traceSampled) {
        ThreadContext.put("severity", "INFO");
        ThreadContext.put("httpMethod", request.getMethod());
        ThreadContext.put("path", request.getRequestURI());
        log.info("Received GET request for /healthz");
        if (hasPayload(request)) {
            ThreadContext.put("severity", "WARN");
            ThreadContext.put("httpMethod", request.getMethod());
            ThreadContext.put("path", request.getRequestURI());
            log.warn("Received request with payload for /healthz");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .headers(httpHeaders())
                    .build();
        }
        try {
            healthCheckService.checkDatabaseConnection();
            ThreadContext.put("severity", "INFO");
            ThreadContext.put("httpMethod", request.getMethod());
            ThreadContext.put("path", request.getRequestURI());
            log.info("Database connected successfully");
            return ResponseEntity.ok()
                    .headers(httpHeaders())
                    .build();
        } catch (Exception e) {
            ThreadContext.put("severity", "ERROR");
            ThreadContext.put("httpMethod", request.getMethod());
            ThreadContext.put("path", request.getRequestURI());
            log.error("Database connection check failed", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .headers(httpHeaders())
                    .build();
        }
    }

    @RequestMapping(value = "/healthz", method = { RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
            RequestMethod.PATCH, RequestMethod.HEAD, RequestMethod.OPTIONS })
    public ResponseEntity<Void> invalidrequestMethod(HttpServletRequest request) {
        ThreadContext.put("severity", "WARN");
        ThreadContext.put("httpMethod", request.getMethod());
        ThreadContext.put("path", request.getRequestURI());
        log.warn("Received invalid request method for /healthz: " + request.getMethod());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .headers(httpHeaders())
                .build();

    }

    @RequestMapping(value = "/**")
    public ResponseEntity<Void> invalidurl(HttpServletRequest request) {
        ThreadContext.put("severity", "WARN");
        ThreadContext.put("httpMethod", request.getMethod());
        ThreadContext.put("path", request.getRequestURI());
        log.warn("Received request for invalid URL: " + request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(httpHeaders())
                .build();

    }

    private boolean hasPayload(HttpServletRequest request) {
        return Objects.nonNull(request.getHeader("Content-Length")) && !request.getHeader("Content-Length").equals("0")
                || !request.getParameterMap().isEmpty();

    }

    private HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        headers.setPragma("no-cache");
        headers.set("X-Content-Type-Options", "nosniff");
        return headers;
    }
}
