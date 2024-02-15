package com.webapplication.Webapp.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserResponse {
    private UUID id;
    private String username;
    private String first_name;
    private String last_name;
    private LocalDateTime accountCreated;
    private LocalDateTime accountUpdated;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public LocalDateTime getAccountCreated() {
        return accountCreated;
    }

    public void setAccountCreated(LocalDateTime accountCreated) {
        this.accountCreated = accountCreated;
    }

    public LocalDateTime getAccountUpdated() {
        return accountUpdated;
    }

    public void setAccountUpdated(LocalDateTime accountUpdated) {
        this.accountUpdated = accountUpdated;
    }

    public UserResponse(UUID id, String username, String first_name, String last_name, LocalDateTime accountCreated, LocalDateTime accountUpdated) {
        this.id = id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.accountCreated = accountCreated;
        this.accountUpdated = accountUpdated;
    }

    public UserResponse() {
    }

    public static UserResponse convertToDTO(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setFirst_name(user.getFirst_name());
        dto.setLast_name(user.getLast_name());
        dto.setUsername(user.getUsername());
        dto.setAccountCreated(user.getAccount_created());
        dto.setAccountUpdated(user.getAccount_updated());
        return dto;
    }
}
