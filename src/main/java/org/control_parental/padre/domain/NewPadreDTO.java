package org.control_parental.padre.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
public class NewPadreDTO {
    @Size(min = 2, max = 50)
    @NonNull
    String nombre;

    @Size(min = 2, max = 50)
    @NonNull
    String apellido;

    @Email
    String email;

    @Size(min = 9, max = 15)
    @NonNull
    String phoneNumber;

    @Size(min = 6, max = 50)
    @NonNull
    String password;
}
