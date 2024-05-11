package org.control_parental.publicacion.domain;

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
