package org.control_parental.salon.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewSalonDTO {

    @NotNull
    @Size(min = 2, max = 50)
    String nombre;
}
