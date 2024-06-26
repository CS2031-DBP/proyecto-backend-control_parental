package org.control_parental.nido.Domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.admin.domain.Admin;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.like.Domain.Padre_Like;
import org.control_parental.padre.domain.Padre;
import org.control_parental.profesor.domain.Profesor;
import org.control_parental.usuario.domain.Usuario;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Nido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToOne
    private Admin admin;

    @OneToMany(mappedBy = "nido")
    List<Padre> padres;

    @OneToMany(mappedBy = "nido")
    List<Profesor> profesores;

    @OneToMany(mappedBy = "nido")
    List<Hijo> estudiantes;

}
