package org.control_parental.salon.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.profesor.dto.ProfesorResponseDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalonResponseDto {

    String nombre;

    @JsonIgnoreProperties("salones")
    List<ProfesorResponseDto> profesores;

    @JsonIgnoreProperties("salon")
    List<PublicacionResponseDto> publicaciones;

    @JsonIgnoreProperties("salon")
    List<HijoResponseDto> hijos;
}
