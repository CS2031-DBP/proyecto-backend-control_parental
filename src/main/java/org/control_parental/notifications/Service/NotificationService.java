package org.control_parental.notifications.Service;

import io.github.jav.exposerversdk.ExpoPushMessage;
import io.github.jav.exposerversdk.ExpoPushTicket;
import io.github.jav.exposerversdk.PushClient;
import io.github.jav.exposerversdk.PushClientException;
import org.control_parental.notifications.Domain.NotificationMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class NotificationService {


    // La version comentada es usando HTTPURLConnection y es "a la mala", pasa que no me funcionaba con el PushClient del
    // sdk y lo hice todo desde cero hasta que me di cuenta que no corria por otra cosa y volvi a usar el PushClient
    // mas que todo para ver que tambien funciona con el sdk, pero la version "a la mala" funciona igual de bien, solo que con 4 veces mas las lineas de codigo
    private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

//    @Async
//    public void sendNotification(NotificationMessage notificationMessage) throws PushClientException {
//        HttpURLConnection connection = null;
//        try {
//            // Create the URL object
//            URL url = new URL(EXPO_PUSH_URL);
//
//            // Open a connection to the URL
//            connection = (HttpURLConnection) url.openConnection();
//
//            // Set the request method to POST
//            connection.setRequestMethod("POST");
//
//            // Set the request headers
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoOutput(true);
//
//            // Create the JSON payload
//            String jsonInputString = String.format(
//                    "{\"to\":\"%s\", \"title\":\"%s\", \"body\":\"%s\"}",
//                    notificationMessage.getRecipientToken(),
//                    notificationMessage.getTitle(),
//                    notificationMessage.getBody()
//            );
//
//            // Write the JSON payload to the connection output stream
//            try (OutputStream os = connection.getOutputStream()) {
//                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
//                os.write(input, 0, input.length);
//            }
//
//            // Get the response code to ensure the request was successful
//            int responseCode = connection.getResponseCode();
//            if (responseCode != HttpURLConnection.HTTP_OK) {
//                System.out.println("response code is not ok");
//            }
//        } catch (Exception e) {
//            System.out.println("Exception while sending notification: " + e.getMessage());
//        } finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//        }
//    }

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
