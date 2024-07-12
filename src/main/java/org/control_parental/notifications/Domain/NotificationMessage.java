package org.control_parental.notifications.Domain;

import lombok.Data;

import java.util.Map;

@Data
public class NotificationMessage {

    private String recipientToken;
    private String title;
    private String body;


    public NotificationMessage(String notificationToken, String title, String body) {
    }
}
