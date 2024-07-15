package org.control_parental.auth.dto;

import lombok.Data;

@Data
public class ForgottenPasswordDto {
    String email;
    String password;
}
