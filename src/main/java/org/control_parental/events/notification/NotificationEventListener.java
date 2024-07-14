package org.control_parental.events.notification;

import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.jav.exposerversdk.PushClientException;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.hijo.dto.HijoResponseDto;
import org.control_parental.hijo.infrastructure.HijoRepository;
import org.control_parental.notifications.Domain.NotificationMessage;
import org.control_parental.notifications.Service.NotificationService;
import org.control_parental.padre.domain.Padre;
import org.control_parental.padre.dto.PadreResponseDto;
import org.control_parental.padre.infrastructure.PadreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationEventListener {

    @Autowired
    HijoRepository hijoRepository;

    @Autowired
    PadreRepository padreRepository;

    @Autowired
    NotificationService notificationService;

    @Async
    @EventListener
    public void sendNotificationToParents(NotificationEvent notificationEvent) {
        List<Hijo> hijos = notificationEvent.getSalon().getHijos();

        List<Padre> padres = new ArrayList<>();
        hijos.forEach(hijo -> {
            Padre padre = hijo.getPadre();
            padres.add(padre);
        });

        padres.forEach(padre -> {
            NotificationMessage message = new NotificationMessage(
                    padre.getNotificationToken(),
                    notificationEvent.getTitle(),
                    notificationEvent.getBody());
            try {
                notificationService.sendNotification(message);
            } catch (FirebaseMessagingException | PushClientException e) {
                throw new RuntimeException(e);
            }
        });
    }



}
