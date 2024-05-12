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

    @NotNull
    String phoneNumber;

    @NotNull
    String email;

    @NotNull
    String nombre;

    @NotNull
    String apellido;

    @JsonIgnoreProperties("padre")
    List<HijoResponseDto> hijos;
}
