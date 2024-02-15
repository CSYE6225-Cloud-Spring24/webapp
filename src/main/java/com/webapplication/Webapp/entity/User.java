package com.webapplication.Webapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String first_name;
    @Column(nullable = false)
    private String last_name;
    @Column(unique = true, nullable = false)
    @Email
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime account_created;
    @UpdateTimestamp
    private LocalDateTime account_updated;

    public User(UUID id, String first_name, String last_name, String username, String password, LocalDateTime account_created, LocalDateTime account_updated) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.account_created = account_created;
        this.account_updated = account_updated;
    }

    public User() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getAccount_created() {
        return account_created;
    }

    public void setAccount_created(LocalDateTime account_created) {
        this.account_created = account_created;
    }

    public LocalDateTime getAccount_updated() {
        return account_updated;
    }

    public void setAccount_updated(LocalDateTime account_updated) {
        this.account_updated = account_updated;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", account_created=" + account_created +
                ", account_updated=" + account_updated +
                '}';
    }

    @PrePersist
    protected void onCreate() {
        this.account_created = LocalDateTime.now();
        this.account_updated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.account_updated = LocalDateTime.now();
    }
}
