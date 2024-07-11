package org.control_parental.email.nuevaContraseña;

import jakarta.mail.MessagingException;
import org.control_parental.email.EmailService;
import org.control_parental.email.EmailThymeleafService;
import org.control_parental.email.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class NuevaContraseñaEmailListener {

    @Autowired
    EmailService emailService;

    @Autowired
    EmailThymeleafService thymeleafService;

    @Value("${spring.mail.username}")
    private String email;

//
//    @EventListener
//    @Async
//    public void sendNuevaContraseñaEmail(NuevaContaseñaEmailEvent event) {
//        emailService.sendEmail(event.getEmail(), "Cambio de contraseña!", event.getNombre() +
//        ", \nPor su seguridad le informamos que el dia " + event.getHora() + " se cambió su contraseña.\nSi usted no fue, " +
//                "comuníquese de inmediato con el administrador de la aplicación." +
//                "\n\nUn saludo,\nequipo de Control Parental" );
//
//    }

    @EventListener
    @Async
    public void sendNuevaContraseñaEmail(NuevaContaseñaEmailEvent event) throws MessagingException, IOException {
        Map<String, Object> properties = new HashMap<>();
        properties.put("nombre", event.getNombre());
        properties.put("hora", event.getHora());
        properties.put("password", event.getPassword());

        Mail mail = Mail.builder()
                .from(email)
                .to(event.getEmail())
                .htmlTemplate(new Mail.HtmlTemplate("nuevaContrasenha", properties))
                .subject("Nueva Contraseña!")
                .build();

        thymeleafService.sendEmail(mail);
    }
}
