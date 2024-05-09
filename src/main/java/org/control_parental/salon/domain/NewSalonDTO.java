package org.control_parental.salon.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewSalonDTO {
    @NotNull
    String nombre;
}
