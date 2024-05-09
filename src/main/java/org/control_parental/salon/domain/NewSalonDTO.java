package org.control_parental.salon.domain;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
public class NewSalonDTO {

    @Size(min = 2, max = 50)
    @NonNull
    String nombre;
}
