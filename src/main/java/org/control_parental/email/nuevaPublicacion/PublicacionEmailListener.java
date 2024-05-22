package org.control_parental.email.nuevaPublicacion;

import org.control_parental.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PublicacionEmailListener {

    @Autowired
        private EmailService emailService;

    @EventListener
    @Async
    public void sendPublicacionEventMail(PublicacionEmailEvent event) {
        emailService.sendEmail(event.getEmail(), "Nueva Publicacion!",
                event.getName() + " ha sido etiquetad@ en una nueva publicacion con el titulo: " + event.getTitulo());
    }

}
