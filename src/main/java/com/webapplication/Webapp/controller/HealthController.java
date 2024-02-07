package com.webapplication.Webapp.controller;

import com.webapplication.Webapp.service.HealthCheckService;
import org.springframework.http.HttpHeaders;
import java.util.Objects;

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

    @GetMapping(value = "/healthz")
    public ResponseEntity<Void> healthCheck(HttpServletRequest request) {

        if (hasPayload(request)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .headers(httpHeaders())
                    .build();
        }
        try {
            healthCheckService.checkDatabaseConnection();

            return ResponseEntity.ok()
                    .headers(httpHeaders())
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .headers(httpHeaders())
                    .build();
        }
    }

    @RequestMapping(value = "/healthz", method={RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.HEAD,RequestMethod.OPTIONS})
    public ResponseEntity<Void> invalidrequestMethod(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .headers(httpHeaders())
                .build();

    }

    @RequestMapping(value = "/**")
    public ResponseEntity<Void> invalidurl(HttpServletRequest request) {
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
