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
                event.getPadreNombre() + ",\n" + event.getNombre() + "ha sido etiquetad@ en una nueva publicacion con el titulo: "
                        + event.getTitulo() + ".\nPara mayor informacion, entre a la aplicación\n\nUn saludo,\nequipo de Control Parental");

    }

}
