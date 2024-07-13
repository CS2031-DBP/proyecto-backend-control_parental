package org.control_parental.usuario;

import lombok.*;

@Data
public class NewPasswordDto {
    String oldPassword;
    String newPassword;
}
