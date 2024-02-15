package com.webapplication.Webapp.controller;

import com.webapplication.Webapp.entity.User;
import com.webapplication.Webapp.entity.UserResponse;
import com.webapplication.Webapp.repository.UserRepository;
import com.webapplication.Webapp.service.HealthCheckService;
import com.webapplication.Webapp.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {

   @Autowired
    public UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HealthCheckService healthCheckService;

    @GetMapping("/v1/user/self")
     public ResponseEntity<UserResponse> fetchUserDetails(@RequestHeader("Authorization") String header) {
        try {
            if (!healthCheckService.VerifyDatabaseConnection()) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
            }

            String token = null;
            String base64Credentials = header.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
            String[] parts = credentials.split(":", 2);
            System.out.println("credentials"+credentials);

            String username = parts[0];
            String password = parts[1];
            System.out.println("username"+username);
            User user = userRepository.findByUsername(username);
            if(user ==null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            boolean ValidCredentials = userService.ValidCredentials(username, password);

            if (ValidCredentials) {

                UserResponse userResponse = UserResponse.convertToDTO(user);

                return ResponseEntity.ok().body(userResponse);
            } 
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/v1/user")
    public ResponseEntity<Object> createUser(@RequestBody User newUser) {
        try {
            if (!healthCheckService.VerifyDatabaseConnection()) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
            }
             
            if (newUser.getUsername()== null || newUser.getUsername().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("{\"error\": \"Email id is mandatory\"}");
            }

            if (newUser.getPassword() == null || newUser.getPassword().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("{\"error\": \"Password is mandatory\"}");
            }
            
            if (newUser.getFirst_name() == null || newUser.getFirst_name().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("{\"error\": \"First Name is mandatory\"}");
            }
            if (newUser.getLast_name() == null || newUser.getLast_name().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("{\"error\": \"Last Name is mandatory\"}");
            }

            if (!CheckValidEmail(newUser.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("{\"error\": \"Invalid Email Address\"}");
            }

            if (newUser.getPassword() != null && !CheckValidPassword(newUser.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("{\"error\": \"Invalid password. Please enter password containing atleast one uppercase, one lowercase, and one digit and minimum length of 8\"}");
            }

            userService.createUser(newUser);
            UserResponse userResponse = UserResponse.convertToDTO(newUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userResponse);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\": \"User Already Exists!!\"}");
        }
    }

    @PutMapping("/v1/user/self")
    public ResponseEntity<Object> updateUser(@RequestBody User newUser, @RequestHeader("Authorization") String header) {
        try {

            String token = null;
            String base64Credentials = header.substring("Basic ".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
            String[] parts = credentials.split(":", 2);

            String username = parts[0];
            String password = parts[1];
            User user = userRepository.findByUsername(username);
            if(user ==null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            boolean ValidCredentials = userService.ValidCredentials(username, password);


            if (ValidCredentials) {
                if (!user.getUsername().equals(newUser.getUsername()) ) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("{\"error\": \"Not Authorized to update username\"}");
                }

                user.setFirst_name(newUser.getFirst_name());
                user.setLast_name(newUser.getLast_name());

                
                if (newUser.getPassword() == null || newUser.getPassword().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("{\"error\": \"Password is mandatory\"}");
                }
                
                if (newUser.getFirst_name() == null || newUser.getFirst_name().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("{\"error\": \"First Name is mandatory\"}");
                }
                
                if (newUser.getLast_name() == null || newUser.getLast_name().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("{\"error\": \"Last Name is mandatory\"}");
                }

                if (newUser.getPassword() != null && !CheckValidPassword(newUser.getPassword())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("{\"error\": \"Invalid password. Please enter password containing atleast one uppercase, one lowercase, and one digit and minimum length of 8\"}");
                }
                
                if (newUser.getPassword() != null && !newUser.getPassword().isEmpty()) {
                    user.setPassword(new BCryptPasswordEncoder().encode((newUser.getPassword())));
                }
                
                user.setAccount_updated(LocalDateTime.now());

                userRepository.save(user);

                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } 
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
   @RequestMapping(value = "/v1/user/self", method = {RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.OPTIONS})
   public ResponseEntity<Void> ExceptGetAndPutInvalidMethod(HttpServletRequest request) {
       if (!healthCheckService.VerifyDatabaseConnection()) {
           return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
       }
       return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
               .cacheControl(CacheControl.noCache())
               .build();
   }

   @RequestMapping(value = "/v1/user", method = {RequestMethod.GET, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.OPTIONS})
   public ResponseEntity<Void> ExceptPostInvalidMethod(HttpServletRequest request) {
       if (!healthCheckService.VerifyDatabaseConnection()) {
           return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
       }
       return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
               .cacheControl(CacheControl.noCache())
               .build();
   }

    private boolean CheckValidEmail(String email) {
    String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
   }
    private boolean CheckValidPassword(String password) {
    String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
    return password.matches(regex);
    }
 
}
