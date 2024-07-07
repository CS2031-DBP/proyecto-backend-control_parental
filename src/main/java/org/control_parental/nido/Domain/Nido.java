package org.control_parental.nido.Domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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


@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity
@Data
@NoArgsConstructor
public class Nido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToOne
    @JsonIgnore
    private Admin admin;

    @OneToMany(mappedBy = "nido")
    List<Usuario> usuarios;
}
