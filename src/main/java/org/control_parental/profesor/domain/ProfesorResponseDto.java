package org.control_parental.profesor.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProfesorResponseDto {
    @NotNull
    String nombre;
    @NonNull
    String apellido;
    @NonNull
    String email;
}
