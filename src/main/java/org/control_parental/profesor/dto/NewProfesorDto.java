package org.control_parental.profesor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewProfesorDto {

    @Size(min = 2, max = 50)
    @NotNull
    String nombre;

    @Size(min = 2, max = 50)
    @NotNull
    String apellido;

    @Email
    String email;

    @Size(min = 6, max = 50)
    @NotNull
    String password;
}
