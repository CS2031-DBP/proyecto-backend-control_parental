package org.control_parental.profesor;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.control_parental.usuario.domain.Usuario;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Profesor extends Usuario {

}
