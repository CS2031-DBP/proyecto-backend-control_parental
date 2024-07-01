package org.control_parental.salon.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.hijo.dto.ReducedHijoDto;
import org.control_parental.profesor.dto.ProfesorResponseDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalonResponseDto {

    @NotNull
    String nombre;

    Long id;
    @JsonIgnoreProperties("salones")
    List<ProfesorResponseDto> profesores;

    @JsonIgnoreProperties("salon")
    List<PublicacionResponseDto> publicaciones;

    List<ReducedHijoDto> hijos;
}
