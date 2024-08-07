package org.control_parental.hijo.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.padre.dto.PadreResponseDto;
import org.control_parental.publicacion.dto.PublicacionResponseDto;
import org.control_parental.salon.dto.HijoSalonDto;
import org.control_parental.salon.dto.ReducedSalonDto;
import org.control_parental.salon.dto.SalonResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PadreHijoResponseDto {

    @NotNull
    private String nombre;

    @NotNull
    private Long id;

    @NotNull
    private String apellido;

    private HijoSalonDto salon;
}