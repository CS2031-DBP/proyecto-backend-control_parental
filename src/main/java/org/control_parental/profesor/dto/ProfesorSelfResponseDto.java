package org.control_parental.profesor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class ProfesorSelfResponseDto {
    @NotNull
    String phoneNumber;
    @NotNull
    @Email
    String email;
    @NotNull
    String password;
    @NotNull
    String nombre;
    @NotNull
    String apellido;

}
