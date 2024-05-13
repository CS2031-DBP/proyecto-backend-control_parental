package org.control_parental.hijo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.dto.NewPadreDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewHijoDto {

    @NotNull
    String nombre;

    @NotNull
    String apellido;

    NewPadreDto padre;

    public NewHijoDto(String nombre, String apellido) {
    }
}
