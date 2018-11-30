package com.ftms.ftmsapi.payload;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.*;

public class SignUpRequest {
    private String id;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
