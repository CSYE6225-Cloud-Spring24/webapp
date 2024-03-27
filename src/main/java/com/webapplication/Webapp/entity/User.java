package com.webapplication.Webapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    // private boolean verified; // New field for verification status
    // private String verificationToken; // New field for verification token
    @JsonProperty("is_verified")
    @Column(nullable = false, name = "Is_Verified")
    private int is_verified; // Use Boolean type
    @Column(name = "verification_expiration")
    private LocalDateTime verification_expiration;

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
        this.is_verified=0;
        this.verification_expiration=LocalDateTime.now().plusMinutes(2);
    }

    @PreUpdate
    protected void onUpdate() {
        this.account_updated = LocalDateTime.now();
    }

    // public boolean isVerified() {
    //     return verified;
    // }

    // public void setVerified(boolean verified) {
    //     this.verified = verified;
    // }

    // public String getVerificationToken() {
    //     return verificationToken;
    // }

    // public void setVerificationToken(String verificationToken) {
    //     this.verificationToken = verificationToken;
    // }
    public int getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(int is_verified) {
        this.is_verified = is_verified;
    }
    public LocalDateTime getVerification_expiration() {
        return verification_expiration;
    }

    public void setVerification_expiration(LocalDateTime verification_expiration) {
        this.verification_expiration = verification_expiration;
    }
}
