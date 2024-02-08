package com.webapplication.Webapp.repository;

import com.webapplication.Webapp.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);

    public boolean existsByUsername(String username);
}
