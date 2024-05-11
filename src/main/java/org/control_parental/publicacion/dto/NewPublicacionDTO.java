package org.control_parental.publicacion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewPublicacionDTO {

    @NotNull
    String titulo;
    @NotNull
    String descripcion;
    @NotNull
    String foto;
}
