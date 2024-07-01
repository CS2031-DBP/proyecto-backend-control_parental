package org.control_parental.padre.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.control_parental.hijo.dto.HijoResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PadreResponseDto {

    Long id;
    String phoneNumber;

    String email;

    String nombre;

    String apellido;

    @JsonIgnoreProperties("padre")
    List<HijoResponseDto> hijos;
}
