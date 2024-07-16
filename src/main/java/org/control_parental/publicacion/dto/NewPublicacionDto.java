package org.control_parental.publicacion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPublicacionDto {

    @NotNull
    String titulo;

    @NotNull
    String descripcion;

    List<Long> hijos_id;

    private Long salonId;

    String ubicacion;

    Double latitud;

    Double longitud;
}
