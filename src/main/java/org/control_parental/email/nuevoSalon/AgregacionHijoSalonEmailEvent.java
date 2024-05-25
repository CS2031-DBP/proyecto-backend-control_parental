package org.control_parental.email.nuevoSalon;

public class AgregacionHijoSalonEmailEvent {
    private String nombre;
    private String email;

    private String salon;

    private String padreNombre;

    public AgregacionHijoSalonEmailEvent(Object source, String nombre, String email, String salon, String padreNombre) {
        super();
        this.nombre = nombre;
        this.email = email;
        this.salon = salon;
        this.padreNombre = padreNombre;
    }
    public String getEmail() {
        return email;
    }

    public String getName() {
        return nombre;
    }

    public String getSalon() {
        return salon;
    }

    public String getPadreNombre() {return padreNombre;}
}
