package org.example.demospringbootangular.controller;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Notification;
import org.example.demospringbootangular.repository.NotificationRepository;
import org.example.demospringbootangular.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getLoggedUserNotifications")
    public ResponseEntity<?> getUserNotifications(Principal principal){

        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Notification> userNotifications = notificationRepository.findByUserId(String.valueOf(user.getId()));
        return ResponseEntity.ok(userNotifications);
    }
}
