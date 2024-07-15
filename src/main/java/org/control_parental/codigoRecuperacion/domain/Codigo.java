package org.control_parental.codigoRecuperacion.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.control_parental.usuario.domain.Usuario;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Codigo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String codigo;
    Date date;

    @OneToOne
    @JsonIgnore
    Usuario usuario;

}
