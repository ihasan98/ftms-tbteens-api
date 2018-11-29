package com.ftms.ftmsapi.controller;

import com.ftms.ftmsapi.model.User;
import com.ftms.ftmsapi.payload.ApiResponse;
import com.ftms.ftmsapi.payload.JwtAuthenticationResponse;
import com.ftms.ftmsapi.payload.LoginRequest;
import com.ftms.ftmsapi.payload.SignUpRequest;
import com.ftms.ftmsapi.repository.UserRepository;
import com.ftms.ftmsapi.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @GetMapping("/user/{id}")
    public ResponseEntity getUser(@PathVariable Long id) {
        try {
            User user = userRepository.getOne(id);
            System.out.println("Found user");
            return new ResponseEntity<Object>(user, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity(new ApiResponse(false, "User not found!"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
//        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
//                    HttpStatus.BAD_REQUEST);
//        }
//
//        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
//                    HttpStatus.BAD_REQUEST);
//        }

        // Creating user's account
        User user = userRepository.getOne(signUpRequest.getId());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setActive(true);
        User result = userRepository.save(user);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }
}

