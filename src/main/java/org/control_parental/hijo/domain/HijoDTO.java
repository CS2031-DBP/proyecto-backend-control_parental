package org.control_parental.hijo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HijoDTO {

    @NonNull
    String nombre;

    @NonNull
    String apellido;

}
