package org.control_parental.notifications.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {

    private String recipientToken;
    private String title;
    private String body;

}
