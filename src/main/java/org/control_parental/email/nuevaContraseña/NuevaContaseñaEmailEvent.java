package org.control_parental.email.nuevaContraseña;

import java.time.ZonedDateTime;
import java.util.Date;

public class NuevaContaseñaEmailEvent {

    private String nombre;
    private String email;

    private Date hora;

    private String password;

    public NuevaContaseñaEmailEvent(String nombre, String email, Date hora, String password) {
        this.nombre = nombre;
        this.email = email;
        this.hora = hora;
        this.password = password;
    }

    public Date getHora() {
        return hora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {return password;}
}
