package org.control_parental.hijo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.control_parental.padre.domain.Padre;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.salon.domain.Salon;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Hijo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id;

    @Column(name = "nombre", nullable = false)
    @Size(min = 2, max = 50)
    String nombre;

    @Column(name = "apellido", nullable = false)
    @Size(min = 2, max = 50)
    String apellido;

    @ManyToOne
    @JoinColumn(name = "padre")
    Padre padre;

    @ManyToMany
    List<Publicacion> publicaciones;

    @ManyToOne
    Salon salon;
}
