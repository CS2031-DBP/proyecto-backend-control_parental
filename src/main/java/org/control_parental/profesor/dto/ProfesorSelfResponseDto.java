package org.control_parental.profesor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.comentario.dto.ComentarioResponseDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.salon.dto.ReducedSalonDto;
import org.control_parental.salon.dto.SalonResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfesorSelfResponseDto {

    @NotNull
    @Email
    String email;

    Long id;
    @NotNull
    String nombre;

    @NotNull
    String apellido;

    @JsonIgnoreProperties("profesores")
    List<ReducedSalonDto> salones;

    @JsonIgnoreProperties("profesor")
    List<ComentarioResponseDto> comentarios;

    @JsonIgnoreProperties("profesor")
    List<PublicacionResponseDto> publicaciones;
}
