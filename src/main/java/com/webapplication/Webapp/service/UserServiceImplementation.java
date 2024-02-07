package com.webapplication.Webapp.service;

import org.springframework.stereotype.Service;
import com.webapplication.Webapp.entity.User;
import com.webapplication.Webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    public UserServiceImplementation(UserRepository userRepository){
        this.userRepository=userRepository;
        this.passwordEncoder=new BCryptPasswordEncoder();
    }

    @Override
    public List<User> fetchUserDetails() {
        return userRepository.findAll();
    }
    
    @Override
    public User createUser(User user) throws Exception {
        String username = user.getUsername();

        if (userRepository.findByUsername(username)==null) {
            String encodedPassword = this.passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                user.setAccount_created(LocalDateTime.now());
                user.setAccount_updated(LocalDateTime.now());
                return userRepository.save(user);
        }
        else {
                throw new Exception("User Exists Already!!");
        }
    }

     public boolean ValidCredentials(String username, String password) {
        User user = userRepository.findByUsername(username);
        return BCrypt.checkpw(password,user.getPassword());
    }
}
