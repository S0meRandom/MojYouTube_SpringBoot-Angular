package org.example.demospringbootangular.Service;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public Notification newCommentNotification(String videoName, String commenterName, AppUser receiver){
        Notification newCommentNotification = new Notification();

        String commentContent = commenterName + " właśnie dodał komentarz do twojego filmiku "+ videoName;
        newCommentNotification.setContent(commentContent);
        newCommentNotification.setUserId(String.valueOf(receiver.getId()));
        newCommentNotification.setIsRead(false);

        return newCommentNotification;
    }
    public Notification newSubscriberNotification(AppUser receiver,String subscriberName){
        Notification newSubscriberNotification = new Notification();

        String commentContent = subscriberName + " właśnie zasubskrybował twój kanał!   ";
        newSubscriberNotification.setUserId(String.valueOf(receiver.getId()));
        newSubscriberNotification.setContent(commentContent);
        newSubscriberNotification.setIsRead(false);
        return newSubscriberNotification;
    }
}
