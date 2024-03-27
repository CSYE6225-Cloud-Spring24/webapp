package com.webapplication.Webapp.service;

import com.webapplication.Webapp.entity.User;

import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {

    public List<User> fetchUserDetails();

    public User createUser(User user)throws Exception;

    public boolean ValidCredentials(String username, String password);

    Optional<User> getUserById(UUID id);

    void saveUser(User user);

}
