package com.registration.Registration.service;

import com.registration.Registration.dao.RegistrationRepository;
import com.registration.Registration.dto.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private RegistrationRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailSendingService emailService;

    public Users registerUser(Users user) {
        if (isEmailTaken(user.getEmail())) {
            throw new RuntimeException("Email is already registered.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Users savedUser =  userRepository.save(user);

        emailService.sendSimpleEmail(savedUser.getEmail(), "Registration Successful",
                "Hello " + savedUser.getUserName() + ",\n\nCongratulations! You have successfully registered with us.");

        return savedUser;
    }

    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean loginUser(String email, String password) {
        Optional<Users> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                emailService.sendSimpleEmail(user.getEmail(), "Hello " + user.getUserName() , ",\n\nYou have been successfully logged into your account");
                return true;
            }
        }
        return false;
    }
}

