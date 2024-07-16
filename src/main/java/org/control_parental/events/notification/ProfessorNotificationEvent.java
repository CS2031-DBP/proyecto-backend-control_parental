package org.control_parental.events.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorNotificationEvent {

    String title;
    String body;
    String token;
}
