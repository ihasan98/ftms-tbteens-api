package com.ftms.ftmsapi.payload;

import javax.validation.constraints.*;

public class SignUpRequest {
    private Long id;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
