package org.control_parental.hijo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewHijoDto {

    @NotNull
    String nombre;

    @NotNull
    String apellido;
}
