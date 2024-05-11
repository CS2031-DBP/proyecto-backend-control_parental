package org.control_parental.hijo.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HijoDTO {

    @NotNull
    String nombre;

    @NotNull
    String apellido;

}
