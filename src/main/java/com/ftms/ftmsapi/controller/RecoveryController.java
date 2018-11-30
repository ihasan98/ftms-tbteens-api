package com.ftms.ftmsapi.controller;

import com.ftms.ftmsapi.model.User;
import com.ftms.ftmsapi.payload.ApiResponse;
import com.ftms.ftmsapi.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/recovery")
public class RecoveryController {
    @Autowired
    UserRepository userRepository;

    // Verify the information given when trying to recover a password
    @PostMapping("/password")
    ResponseEntity<?> verifyRecoverPassword(@Valid @RequestBody String jsonData) {
        // Parse string as JSON and parse keys
        System.out.println(jsonData);
        JSONObject data = new JSONObject(jsonData);
        String email = (String) data.get("email");

        // Find the user by email
        for (User user: userRepository.findAll()) {
            // Return OK if found
            if (user.getEmail().equals(email)) {
                return new ResponseEntity(new ApiResponse(true, "User found. Proceed below!"), HttpStatus.OK);
            }
        }
        // Return not found otherwise
        return new ResponseEntity(new ApiResponse(false, "User not found"), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/emailboth")
    ResponseEntity<?> verifyRecoverEmailOrBoth(@Valid @RequestBody String jsonData) {
        // Parse string as JSON and keys
        JSONObject data = new JSONObject(jsonData);
        String phone = (String) data.get("phone");
        String firstName = (String) data.get("firstname");
        String lastName = (String) data.get("lastname");

        // Look for the user with the phone, first name and last name combo as above
        for (User user: userRepository.findAll()) {
            if (user.getNumber().equals(phone) &&
                    user.getFirstname().equals(firstName) &&
                        user.getLastname().equals(lastName)) {
                // Found gives OK response
                return new ResponseEntity(new ApiResponse(true, "User found. Proceed below!"), HttpStatus.OK);
            }
        }
        // Not found gives not found response
        return new ResponseEntity(new ApiResponse(false, "User not found"), HttpStatus.NOT_FOUND);
    }
}
