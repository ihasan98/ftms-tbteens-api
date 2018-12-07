package com.ftms.ftmsapi.controller;
import com.ftms.ftmsapi.payload.UserSummary;
import com.ftms.ftmsapi.repository.UserRepository;
import com.ftms.ftmsapi.model.User;
import com.ftms.ftmsapi.security.CurrentUser;
import com.ftms.ftmsapi.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserRepository<User> userRepository;

    @GetMapping("/me")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        System.out.println(currentUser.getEmail());
        System.out.println(currentUser.getUsername());
        Long companyId = (currentUser.getCompany() != null) ?
                currentUser.getCompany().getId() : null;
        UserSummary userSummary = new UserSummary(currentUser.getId(),
                currentUser.getEmail(), currentUser.getFirstname(),
                currentUser.getLastname(), currentUser.getRole(), currentUser.getNumber(),
                companyId);
        return userSummary;
    }
}
