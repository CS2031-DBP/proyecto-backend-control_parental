package org.control_parental.email.nuevoSalon;

public class AgregacionHijoSalonEmailEvent {
    private String nombre;
    private String email;

    private String salon;

    public AgregacionHijoSalonEmailEvent(Object source, String nombre, String email, String salon) {
        super();
        this.nombre = nombre;
        this.email = email;
        this.salon = salon;
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
}
