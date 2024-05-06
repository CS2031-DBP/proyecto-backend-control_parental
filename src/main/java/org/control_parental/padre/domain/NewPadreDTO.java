package org.control_parental.padre.domain;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class NewPadreDTO {
    String nombre;

    String apellido;

    @Email
    String email;

    String password;
}
