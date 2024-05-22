package org.control_parental.hijo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.control_parental.padre.dto.PadreResponseDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.salon.dto.SalonResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HijoPublicacionDto {

    @NotNull
    private String nombre;

    @NotNull
    private String apellido;

}
