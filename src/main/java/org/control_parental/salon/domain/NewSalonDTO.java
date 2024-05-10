package org.control_parental.salon.domain;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewSalonDTO {

    @NotNull
    @Size(min = 2, max = 50)
    String nombre;
}
