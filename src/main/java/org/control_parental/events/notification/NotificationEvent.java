package org.control_parental.events.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.control_parental.hijo.domain.Hijo;
import org.control_parental.salon.domain.Salon;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NotificationEvent {

    Salon salon;
    String title;
    String body;
}
