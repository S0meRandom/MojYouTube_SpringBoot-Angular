package org.example.demospringbootangular.ControllerTests;

import org.example.demospringbootangular.controller.NotificationController;
import org.example.demospringbootangular.model.Notification;
import org.example.demospringbootangular.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Optional;

import static com.mongodb.assertions.Assertions.assertTrue;
import static javax.security.auth.callback.ConfirmationCallback.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTests {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    void shouldSetNotificationAsRead(){
        String notificationId = "5";
        Notification mockNotification = new Notification();
        mockNotification.setId(notificationId);
        mockNotification.setIsRead(false);
        mockNotification.setContent("Ktoś skomentował Twój film");

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(mockNotification));

        assertTrue(mockNotification.getIsRead());
        verify(notificationRepository,times(1)).save(mockNotification);

    }
}
