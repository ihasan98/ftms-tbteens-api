package com.ftms.ftmsapi.controller;

import com.ftms.ftmsapi.model.ClientUser;
import com.ftms.ftmsapi.model.Company;
import com.ftms.ftmsapi.repository.CompanyRepository;
import com.ftms.ftmsapi.services.EmailService;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/companies")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class CompanyController {
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    private EmailService emailService;

    Hashids hashids = new Hashids("FTMS", 10);

    /**
     * Get all companies in a list.
     *
     * @return A list containing all companies.
     */
    @GetMapping("")
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    /**
     * Save company to the repository and then return it.
     *
     * @param company The company to be saved.
     * @return
     */
    @PostMapping("")
    public Company createCompany(@Valid @RequestBody Company company) {
        // Hashing ClientUser id
        Company createdCompany = companyRepository.save(company);
        String id = hashids.encode(createdCompany.getId());
        String content = emailService.getCompanyRegistrationContent(createdCompany.getName(),
                "http://localhost:3000/companysignup/" + id);
        emailService.sendEmail(createdCompany.getName(), createdCompany.getEmail(), content,
                "Nor-Weld Company Account Registration");
        return createdCompany;
    }

    /**
     * Delete the company with ID id, and then return the response entity.
     *
     * @param id The ID of the company to be deleted.
     * @return The response entity from the system.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee (@PathVariable Long id) {
        try {
            Company company = companyRepository.getOne(id);
            companyRepository.delete(company);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Create a new empty list,
     * Edit the credentials of the company with ID id
     * AND RETURN THE SAME EMPTY LIST CREATED AT THE BEGINNING.
     *
     * @param id The ID of the company to be changed.
     * @param name The new name of the company.
     * @param logo The logo of the company.
     * @param email The email of the company.
     * @param phone The phone number of the company.
     * @return An empty list.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editCompany (@PathVariable Long id, @RequestParam String name,
                                               @RequestParam String logo, @RequestParam String email,
                                               @RequestParam String phone) {
        ArrayList<String> value = new ArrayList<>();
        try {
            Company findCompany = companyRepository.getOne(id);
            findCompany.setName(name);
            findCompany.setLogo(logo);
            findCompany.setEmail(email);
            findCompany.setNumber(phone);
            System.out.println(findCompany.getName());
            companyRepository.save(findCompany);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
