package org.control_parental.profesor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVRecord;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewProfesorDto {

    @Size(min = 2, max = 50)
    @NotNull
    String nombre;

    @Size(min = 2, max = 50)
    @NotNull
    String apellido;

    @Email
    String email;

    public static NewProfesorDto parse(CSVRecord record) {
        return new NewProfesorDto(record.get("Nombre"), record.get("Apellido"), record.get("Email"));
    }
}
















