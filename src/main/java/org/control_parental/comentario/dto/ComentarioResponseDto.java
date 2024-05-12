package org.control_parental.comentario.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.padre.dto.PadreResponseDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioResponseDto {

    LocalDateTime fecha;

    String contenido;

    PadreResponseDto padre;

    @JsonIgnoreProperties("comentarios")
    PublicacionResponseDto publicacion;
}
