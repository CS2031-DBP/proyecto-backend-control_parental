package org.control_parental.padre.dto;

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
public class NewPadreDto {

//    @Size(min = 2, max = 50)
    @NotNull
    String nombre;

//    @Size(min = 2, max = 50)
    @NotNull
    String apellido;

    @Email
    String email;

//    @Size(min = 9, max = 15)
    @NotNull
    String phoneNumber;

//    @Size(min = 6, max = 50)
    @NotNull
    String password;

    public static NewPadreDto parse(CSVRecord record) {
        return new NewPadreDto(record.get("Nombre"),
                record.get("Apellido"),
                record.get("Email"),
                record.get("phoneNumber"),
                record.get("Password"));

    }
}
