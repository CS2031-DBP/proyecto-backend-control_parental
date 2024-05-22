package org.control_parental.hijo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.dto.NewPadreDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewHijoDto {

    @NotNull
    String nombre;

    @NotNull
    String apellido;

    String email;

}
