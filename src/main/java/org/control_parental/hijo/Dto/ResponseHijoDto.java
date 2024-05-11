package org.control_parental.hijo.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ResponseHijoDto {
    @NotNull
    private Long id;
    @NotNull
    private String nombre;
    @NotNull
    private String apellido;
}
