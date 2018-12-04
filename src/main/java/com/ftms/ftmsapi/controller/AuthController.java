package com.ftms.ftmsapi.controller;

import com.ftms.ftmsapi.model.ClientUser;
import com.ftms.ftmsapi.model.Company;
import com.ftms.ftmsapi.model.Employee;
import com.ftms.ftmsapi.payload.*;
import com.ftms.ftmsapi.repository.ClientUserRepository;
import com.ftms.ftmsapi.repository.CompanyRepository;
import com.ftms.ftmsapi.repository.EmployeeRepository;
import com.ftms.ftmsapi.security.JwtTokenProvider;

import org.hashids.Hashids;
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

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ClientUserRepository clientUserRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    Hashids hashids = new Hashids("FTMS", 10);

    @GetMapping("/user/{id}")
    public ResponseEntity getEmployee(@PathVariable String id) {
        // Decoding the user id
        long[] longId = hashids.decode(id);
        Long newId = longId[0];
        try {
            Employee employee = employeeRepository.getOne(newId);
            return new ResponseEntity<Object>(employee, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<Object>(new ApiResponse(false, "User not found!"),
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
        // Creating user's account
        // Decoding the user id
        long[] longId = hashids.decode(signUpRequest.getId());
        Long id = longId[0];

        Employee employee = employeeRepository.getOne(id);
        employee.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        employee.setActive(true);
        Employee result = employeeRepository.save(employee);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    @PostMapping("/companysignup")
    public ResponseEntity registerCompany(@Valid @RequestBody CompanySignUpRequest signUpRequest) {
        long[] longId = hashids.decode(signUpRequest.getId());
        Long id = longId[0];
        Company company = companyRepository.getOne(id);
        ClientUser user = new ClientUser(signUpRequest.getFirstname(),
                signUpRequest.getLastname(), signUpRequest.getEmail(),
                signUpRequest.getNumber(), "ROLE_CLIENT", company);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setActive(true);
        ClientUser result = clientUserRepository.save(user);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }
}

