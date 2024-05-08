package org.control_parental.profesor.domain;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class NewProfesorDTO {

    String nombre;

    String apellido;

    @Email
    String email;

    String password;
}
