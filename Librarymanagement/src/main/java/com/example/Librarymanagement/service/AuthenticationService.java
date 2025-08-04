package com.example.Librarymanagement.service;

import com.example.Librarymanagement.JWT.JWTService;
import com.example.Librarymanagement.dto.LoginRequestDTO;
import com.example.Librarymanagement.dto.LoginResponseDTO;
import com.example.Librarymanagement.dto.RegisterRequestDTO;
import com.example.Librarymanagement.entity.User;
import com.example.Librarymanagement.repo.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    public AuthenticationService(UserRepo userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public User registerNormalUser(RegisterRequestDTO registerRequestDTO) {
        if(userRepo.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists with username: " + registerRequestDTO.getUsername());
        }

        Set<String> roles = new HashSet<String>();
        roles.add("ROLE_USER");

        User user = new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRoles(roles);

        return userRepo.save(user);
    }

    public User registerAdminUser(RegisterRequestDTO registerRequestDTO) {

        if(userRepo.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists with username: " + registerRequestDTO.getUsername());
        }

        Set<String> roles = new HashSet<String>();
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_USER");

        User user = new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRoles(roles);

        return userRepo.save(user);

    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
         authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(
                         loginRequestDTO.getUsername(),
                         loginRequestDTO.getPassword()
                 )
         );

        User user = userRepo.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + loginRequestDTO.getUsername()));


        String token = jwtService.generateToken(user);

        return LoginResponseDTO.builder()
                .token(token)
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();


    }
}

