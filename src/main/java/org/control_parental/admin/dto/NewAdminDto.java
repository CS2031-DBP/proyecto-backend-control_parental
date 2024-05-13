package org.control_parental.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewAdminDto {
    @NotNull
    String email;

    @NotNull
    String password;

    @NotNull
    String nombre;

    @NotNull
    String apellido;
}
