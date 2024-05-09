package org.control_parental.comentario.domain;

import lombok.Data;
import lombok.NonNull;

@Data
public class NewComentarioDTO {

    @NonNull
    String contenido;
}
