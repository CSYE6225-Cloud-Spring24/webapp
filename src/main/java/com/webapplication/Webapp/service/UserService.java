package com.webapplication.Webapp.service;

import com.webapplication.Webapp.entity.User;

import org.springframework.http.ResponseEntity;


import java.util.List;


public interface UserService {

    public List<User> fetchUserDetails();

    public User createUser(User user)throws Exception;

    public boolean ValidCredentials(String username, String password);

}
