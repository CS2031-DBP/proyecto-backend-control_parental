package org.control_parental.auth.dto;

import lombok.Data;

@Data
public class AuthLoginRequest {
    private String email;
    private String password;

}
