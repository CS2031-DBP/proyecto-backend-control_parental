package org.control_parental.comentario.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.padre.dto.PadreResponseDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.usuario.domain.Usuario;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioResponseDto {


    LocalDateTime fecha;

    @NotNull
    String contenido;

    @NotNull
    Usuario usuario;

    @JsonIgnoreProperties("comentarios")
    PublicacionResponseDto publicacion;
}
