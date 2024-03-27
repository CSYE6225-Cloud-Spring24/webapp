package com.webapplication.Webapp.repository;

import com.webapplication.Webapp.entity.EmailTracking;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTrackingRepository extends JpaRepository<EmailTracking, UUID> {
    // Custom query methods can be defined here if needed
}
