package com.registration.Registration.dao;

import com.registration.Registration.dto.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
}
