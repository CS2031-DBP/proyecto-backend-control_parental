package org.control_parental.profesor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.control_parental.salon.dto.SalonResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfesorPublicacionDto {

    @NotNull
    String nombre;

    @NotNull
    String apellido;

    @NotNull
    String email;

}
