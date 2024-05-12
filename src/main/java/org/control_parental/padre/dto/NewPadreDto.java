package org.control_parental.padre.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPadreDto {

    @Size(min = 2, max = 50)
    @NotNull
    String nombre;

    @Size(min = 2, max = 50)
    @NotNull
    String apellido;

    @Email
    String email;

    @Size(min = 9, max = 15)
    @NotNull
    String phoneNumber;

    @Size(min = 6, max = 50)
    @NotNull
    String password;
}
