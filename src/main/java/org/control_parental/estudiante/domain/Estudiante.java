package org.control_parental.estudiante.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.padre.domain.Padre;

@NoArgsConstructor
@Data
@Entity
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String nombre;
    String apellido;
    Integer edad;

    @OneToOne
    Padre padre;


}
