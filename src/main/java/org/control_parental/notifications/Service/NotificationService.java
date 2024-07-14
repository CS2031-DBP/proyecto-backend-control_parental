package org.control_parental.notifications.Service;

import io.github.jav.exposerversdk.*;
import org.control_parental.notifications.Domain.NotificationMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class NotificationService {

    @Async
    public void sendNotification(NotificationMessage notificationMessage) throws PushClientException {
        ExpoPushMessage expoPushMessage = new ExpoPushMessage();
        expoPushMessage.getTo().add(notificationMessage.getRecipientToken());
        expoPushMessage.setTitle(notificationMessage.getTitle());
        expoPushMessage.setBody(notificationMessage.getBody());

        List<ExpoPushMessage> messages = List.of(expoPushMessage);

        PushClient client = new PushClient();

        client.sendPushNotificationsAsync(messages);
    }
}
