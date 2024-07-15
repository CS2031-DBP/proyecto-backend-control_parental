package org.control_parental.notifications;

import com.google.auto.value.AutoBuilder;
import io.github.jav.exposerversdk.PushClientException;
import org.apache.catalina.connector.Response;
import org.control_parental.notifications.Domain.NotificationMessage;
import org.control_parental.notifications.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// endpoint de prueba, no pertenece a la logica de negocios
@RestController
@RequestMapping("/notificacion")
public class NotificacionController {

    @Autowired
    NotificationService notificationService;

    @PostMapping("/nueva")
    public ResponseEntity<Void> sendNotificacion(@RequestBody NotificationMessage message) throws PushClientException {
        notificationService.sendNotification(message);
        return ResponseEntity.ok().build();
    }

}
