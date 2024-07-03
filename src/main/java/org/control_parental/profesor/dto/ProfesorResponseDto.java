package org.control_parental.profesor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.control_parental.salon.dto.ReducedSalonDto;
import org.control_parental.salon.dto.SalonResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfesorResponseDto {

    @NotNull
    String nombre;

    Long id;
    @NotNull
    String apellido;

    @NotNull
    String email;

    @JsonIgnoreProperties("profesores")
    List<ReducedSalonDto> salones;
}
