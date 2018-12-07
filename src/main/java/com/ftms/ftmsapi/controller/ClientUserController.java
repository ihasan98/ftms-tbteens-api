package com.ftms.ftmsapi.controller;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.ftms.ftmsapi.model.ClientUser;
import com.ftms.ftmsapi.payload.ApiResponse;
import com.ftms.ftmsapi.repository.ClientUserRepository;
import com.ftms.ftmsapi.services.EmailService;

import com.ftms.ftmsapi.model.Company;
import com.ftms.ftmsapi.repository.CompanyRepository;
import org.hashids.Hashids;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ClientUserController {
    @Autowired
    ClientUserRepository clientUserRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    private EmailService emailService;

    Hashids hashids = new Hashids("FTMS", 10);

    // Get all client users of company
    @GetMapping("/{companyId}")
    public List<ClientUser> getAllclientUsersByCompanyID(@PathVariable Long companyId) {
        ArrayList<ClientUser> users = (ArrayList<ClientUser>) clientUserRepository.findAll();
        ArrayList<ClientUser> nonAdmin = new ArrayList<>();
        // For every ClientUser
        for (ClientUser user: users) {
            // if user in company
            if (user.getCompany().getID().equals(companyId)) {
                // add to list of users to display
                nonAdmin.add(user);
            }
        }
        return nonAdmin;
    }

    // Create a new clientUser.
    @PostMapping("")
    public ClientUser createClientUser(@Valid @RequestBody ClientUser user) {
        // Hashing ClientUser id
        ClientUser createdUser = clientUserRepository.save(user);
        String id = hashids.encode(createdUser.getID());

        emailService.prepareAndSend(createdUser.getEmail(),
                "Account Activation",
                "Please visit http://localhost:3000/usersignup/" + id + " " +
                        "to set up your account");
        return clientUserRepository.save(createdUser);
    }

    // Delete an client user.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClientUser (@PathVariable Long id) {
        try {
            // Try to look for client user by <id>
            ClientUser clientUser = clientUserRepository.getOne(id);
            clientUserRepository.delete(clientUser);
            return new ResponseEntity<Object>(new ApiResponse(true, clientUser.getFirstName()
                    + " " + clientUser.getLastName() + " deleted!"), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // If cannot find, return bad request response
            return new ResponseEntity<Object>(new ApiResponse(true, "This client user does not exist!"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    // Edit an clientUser
    @PutMapping("")
    public ResponseEntity<?> editclientUser (@Valid @RequestBody String info) {
        // Parse string into JSON
        JSONObject clientUser = new JSONObject(info);

        // Parse the keys
        Long id = Long.parseLong(clientUser.get("id").toString());
        String firstName = (String) clientUser.get("firstname");
        String lastName = (String) clientUser.get("lastname");
        String phone = (String) clientUser.get("number");
        Company company = (Company) companyRepository.findById(Long.parseLong(clientUser.get("companyId").toString())).get();

        // Success/Failure messages
        String success = "Edited clientUser #" + id.toString() + " " +
                firstName + " " + lastName + " successfully!";

        String failure = "Cannot find clientUser #" + id.toString();
        try {
            // Try to find the ClientUser by <id>
            ClientUser findClientUser = clientUserRepository.getOne(id);

            // Success: set the info to new info
            findClientUser.setFirstName(firstName);
            findClientUser.setLastName(lastName);
            findClientUser.setNumber(phone);
            findClientUser.setCompany(company);
            clientUserRepository.save(findClientUser);
            return new ResponseEntity<Object>(new ApiResponse(true, success), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // If cannot find, return bad request response
            return new ResponseEntity<Object>(new ApiResponse(true, failure), HttpStatus.BAD_REQUEST);
        }
    }

}
