package com.ftms.ftmsapi.controller;
import com.ftms.ftmsapi.payload.UserSummary;
import com.ftms.ftmsapi.repository.UserRepository;
import com.ftms.ftmsapi.security.CurrentUser;
import com.ftms.ftmsapi.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/me")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(),
                currentUser.getUsername(), currentUser.getFirstname(),
                currentUser.getLastname(), currentUser.getRole(), currentUser.getIsActive());
        return userSummary;
    }
}
