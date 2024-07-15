package org.control_parental.email.codigoVerificacion;

import jakarta.mail.MessagingException;
import org.control_parental.email.EmailService;
import org.control_parental.email.EmailThymeleafService;
import org.control_parental.email.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
public class CodigoVerificacionEmailListener {

    @Autowired
    EmailService emailService;

    @Autowired
    EmailThymeleafService emailThymeleafService;

//    @EventListener
//    @Async
//    public void sendCodigoVerificacion(CodigoVerificacionEmailEvent event) {
//        emailService.sendEmail(event.getEmail(), "Código de verificación", event.getNombre()
//        + "\nSu código de verificación es: " + event.getCodigo() + ".\n\n Un saludo, \nEquipo de Control Parental.");
//    }

    @Value("${spring.mail.username}")
    private String email;
    @EventListener
    @Async
    public void sendCodigoVerificacion(CodigoVerificacionEmailEvent event) throws MessagingException, IOException {
        Map<String, Object> properties = new HashMap<>();
        properties.put("nombre", event.getNombre());
        properties.put("codigo", event.getCodigo());

        Mail mail = Mail.builder()
                .from(email)
                .to(event.getEmail())
                .htmlTemplate(new Mail.HtmlTemplate("codigoRecuperacion", properties))
                .subject("¡Código de recuperación!")
                .build();

        emailThymeleafService.sendEmail(mail);
    }


}
