package org.control_parental.salon.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.control_parental.profesor.dto.ProfesorPublicacionDto;
import org.control_parental.profesor.dto.ProfesorResponseDto;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class HijoSalonDto {
    Long id;
    String nombre;

    List<ProfesorPublicacionDto> profesores;
}
