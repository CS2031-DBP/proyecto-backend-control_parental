package org.control_parental.reply.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.comentario.dto.ComentarioResponseDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.usuario.dto.UsuarioResponseDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyResponseDto {

    LocalDateTime fecha;

    @NotNull
    Long id;

    @NotNull
    String contenido;

    @NotNull
    UsuarioResponseDto usuario;

    //@JsonIgnoreProperties("replies")
    //ComentarioResponseDto comentario;
}

