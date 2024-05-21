package org.control_parental.email.nuevaPublicacion;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class PublicacionEmailEvent extends ApplicationEvent {

    private String nombre;
    private String email;

    private String titulo;

    public PublicacionEmailEvent(Object source, String nombre, String email, String titulo) {
        super(source);
        this.nombre = nombre;
        this.email = email;
        this.titulo = titulo;
    }
    public String getEmail() {
        return email;
    }

    public String getName() {
        return nombre;
    }

    public String getTitulo() {return titulo;}


}
