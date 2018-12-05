package com.ftms.ftmsapi.controller;

import com.ftms.ftmsapi.model.User;
import com.ftms.ftmsapi.payload.ApiResponse;
import com.ftms.ftmsapi.repository.UserRepository;
import com.ftms.ftmsapi.services.EmailService;
import com.ftms.ftmsapi.services.RecoveryCodeService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/api/recovery")
public class RecoveryController {
    @Autowired
    UserRepository<User> userRepository;

    @Autowired
    RecoveryCodeService recoveryCodeService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    // Verify the information given when trying to recover a password
    @PostMapping("/password")
    public ResponseEntity<?> verifyRecoverPassword(@Valid @RequestBody String jsonData) {
        // Parse string as JSON and parse keys
        System.out.println(jsonData);
        JSONObject data = new JSONObject(jsonData);
        String email = (String) data.get("email");

        // Find the user by email
        for (User user: userRepository.findAll()) {
            // Return OK if found
            if (user.getEmail().equals(email)) {
                return new ResponseEntity<Object>(new ApiResponse(true, "User found. Proceed below!"),
                        HttpStatus.OK);
            }
        }
        // Return not found otherwise
        return new ResponseEntity<Object>(new ApiResponse(false, "User not found"), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/emailboth")
    public ResponseEntity<?> verifyRecoverEmailOrBoth(@Valid @RequestBody String jsonData) {
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
                return new ResponseEntity<Object>(new ApiResponse(true, "User found. Proceed below!"),
                        HttpStatus.OK);
            }
        }
        // Not found gives not found response
        return new ResponseEntity<Object>(new ApiResponse(false, "User not found"),
                HttpStatus.NOT_FOUND);
    }

    @PostMapping("/getcode/password/{id}")
    public ResponseEntity<?> createAndSendRecoveryCodeForPassword(@PathVariable Long id) {
        System.out.println("getting code");
        return recoveryCodeService.createNewRecoveryCode(id, "email");
    }

    @PostMapping("/getcode/email/{id}")
    public ResponseEntity<?> createAndSendRecoveryCodeForEmail(@PathVariable Long id) {
        return recoveryCodeService.createNewRecoveryCode(id, "phone");
    }

    @PostMapping("/verifycode/password")
    public ResponseEntity<?> verifyCodeEnteredForRecovery(@Valid @RequestBody String info) {
        JSONObject infoEntered = new JSONObject(info);
        Long userID = Long.parseLong(infoEntered.get("info").toString());
        String code = infoEntered.get("code").toString();
        return recoveryCodeService.verifyCodeEntry(userID, code);
    }

    @GetMapping("/getemail/{id}")
    public HashMap<String, Object> retrieveEmail(@PathVariable Long id) {
        try {
            String email = userRepository.getOne(id).getEmail();
            HashMap<String, Object> result = new HashMap<>();
            result.put("email", email);
            result.put("response", new ResponseEntity<Object>(new ApiResponse(true,
                    "Email has been returned."), HttpStatus.ACCEPTED));
            return result;
        } catch (EntityNotFoundException error) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("response", new ResponseEntity<Object>(new ApiResponse(false,
                    "User #" + id.toString() + " not found."), HttpStatus.BAD_REQUEST));
            return result;
        }
    }

    @PostMapping("/getuserid/name")
    public ResponseEntity<?> getUserIdByName(@Valid @RequestBody String names) {
        JSONObject userInfo = new JSONObject(names);
        String firstName = userInfo.get("firstname").toString();
        String lastName = userInfo.get("lastname").toString();

        for (User user : userRepository.findAll()) {
            if (user.getFirstname().equals(firstName) && user.getLastname().equals(lastName)) {
                return new ResponseEntity<>(user.getId(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/getuserid/email")
    public ResponseEntity<?> getUserIdByEmail(@Valid @RequestBody String email) {
        JSONObject userInfo = new JSONObject(email);
        String emailInfo = userInfo.get("email").toString();

        for (User user : userRepository.findAll()) {
            if (user.getEmail().equals(emailInfo)) {
                return new ResponseEntity<>(user.getId(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/changepassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody String info) {
        // parse json
        JSONObject userInfo = new JSONObject(info);

        // get password entered
        String passwordEntered = userInfo.get("password").toString();

        // get user id
        long userId = Long.parseLong(userInfo.get("id").toString());

        try {
            // find user
            User user = userRepository.getOne(userId);

            // encode password
            user.setPassword(passwordEncoder.encode(passwordEntered));

            userRepository.save(user);
            String content = emailService.getPasswordChangedContent(user.getFirstname());
            emailService.sendEmail(user.getFirstname(), user.getEmail(), content, "Account Information Changed");
            return new ResponseEntity<Object>(new ApiResponse(true,
                    "Password changed successfully!"),
                    HttpStatus.OK);
        } catch (EntityNotFoundException error) {
            return new ResponseEntity<Object>(new ApiResponse(false,
                    "Cannot find employee #" + userId + "!"),
                    HttpStatus.OK);
        }
    }

}
