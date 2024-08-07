package org.control_parental.profesor.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.control_parental.publicacion.domain.Publicacion;
import org.control_parental.salon.domain.Salon;
import org.control_parental.usuario.domain.Usuario;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
public class Profesor extends Usuario {

    @OneToMany(cascade = CascadeType.REMOVE)
    List<Publicacion> publicaciones = new ArrayList<>();

    @ManyToMany
    List<Salon> salones = new ArrayList<>();


    public void addSalon(Salon salon) {salones.add(salon);}

    public void removeSalon(Salon salon) {salones.remove(salon);}

    public void removePublicacion(Publicacion publicacion) {publicaciones.remove(publicacion);}
}
