package org.control_parental.hijo.domain;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewHijoDTO {

    @NonNull
    String nombre;
    @NonNull
    String apellido;

}
