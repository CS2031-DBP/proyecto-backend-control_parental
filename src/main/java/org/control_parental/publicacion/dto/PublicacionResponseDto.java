package org.control_parental.publicacion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.comentario.dto.ComentarioPublicacionDto;
import org.control_parental.hijo.dto.HijoPublicacionDto;
import org.control_parental.padre.dto.PadrePublicacionDto;
import org.control_parental.profesor.dto.ProfesorResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionResponseDto {

    LocalDateTime fecha;
    @NotNull
    Long id;

    @NotNull
    String descripcion;

    @NotNull
    Integer likes;

    List<String> fotos;

    @NotNull
    String titulo;

    @JsonIgnoreProperties("salones")
    ProfesorResponseDto profesor;

    List<HijoPublicacionDto> hijos;

    @JsonIgnoreProperties("publicacion")
    List<ComentarioPublicacionDto> comentarios;

    List<PadrePublicacionDto> likers;

    Long salonId;

    String ubicacion;

    Double latitud;

    Double longitud;
}
