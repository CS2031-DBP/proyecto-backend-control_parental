package org.control_parental.padre.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PadreResponseDto {
    @NotNull
    String phoneNumber;
    @NotNull
    String email;
    @NotNull
    String nombre;
    @NotNull
    String apellido;

}
