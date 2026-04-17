package org.example.demospringbootangular.ServiceTests;

import org.example.demospringbootangular.Service.NotificationService;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NotificationServiceTest {

    private NotificationService notificationService;

    @BeforeEach
    void setUp(){
        notificationService = new NotificationService();
    }

    @Test
    void shouldCreateNotificationCommentSuccessfully(){
        String videoName = "Moje Kotki";
        String commenterName = "Kamil";
        AppUser receiver = new AppUser();
        receiver.setId(23L);

        Notification result = notificationService.newCommentNotification(videoName,commenterName,receiver);
        String expectedContent = "Kamil właśnie dodał komentarz do twojego filmikuMoje Kotki";

        assertNotNull(result);
        assertEquals("23",result.getUser_id());
        assertEquals(expectedContent,result.getContent());



    }

    @Test
    void shouldCreateSubscriberSuccessfully(){
        String subscriberName = "Kamil";
        AppUser receiver = new AppUser();
        receiver.setId(23L);

        Notification result = notificationService.newSubscriberNotification(receiver,subscriberName);
        String expectedContent = "Kamil właśnie zasubskrybował twój kanał!";

        assertNotNull(result);
        assertEquals(expectedContent,result.getContent());
        assertEquals("23",result.getUser_id());
    }
}
