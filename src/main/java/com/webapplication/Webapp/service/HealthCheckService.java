package com.webapplication.Webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService { 
     @Autowired
    private JdbcTemplate jdbcTemplate;

    public void checkDatabaseConnection() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        } catch (Exception e) {
            throw new RuntimeException("Unable to establish a connection to the database", e);
        }
    }
    public boolean VerifyDatabaseConnection() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return true;
        }
        catch (Exception e) {
            return false;
        }
}
}
