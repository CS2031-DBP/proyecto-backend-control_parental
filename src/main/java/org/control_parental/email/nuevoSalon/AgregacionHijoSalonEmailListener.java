package org.control_parental.email.nuevoSalon;

import org.control_parental.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AgregacionHijoSalonEmailListener {

    @Autowired
    private EmailService emailService;

    @EventListener
    @Async
    public void sendAgregacionHijoSalonEmail(AgregacionHijoSalonEmailEvent event) {
        emailService.sendEmail(event.getEmail(), "Nuevo Salon!",
                event.getName() + " ha sido agregado al salon " + event.getSalon()
        + ". Ingrese a la aplicacion para poder ver más detalles!");
        }

    }
