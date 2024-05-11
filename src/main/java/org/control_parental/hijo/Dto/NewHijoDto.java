package org.control_parental.hijo.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewHijoDto {

    @NotNull
    String nombre;

    @NotNull
    String apellido;

}
