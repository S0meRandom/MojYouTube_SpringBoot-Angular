package org.example.demospringbootangular.controller;

import jakarta.transaction.Transactional;
import org.example.demospringbootangular.Service.UserService;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Channel;
import org.example.demospringbootangular.model.RegistrationDTO;
import org.example.demospringbootangular.model.Subscription;
import org.example.demospringbootangular.repository.ChannelRepository;
import org.example.demospringbootangular.repository.SubscriptionRepository;
import org.example.demospringbootangular.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDTO dto){
        userService.registerUser(dto);
        return ResponseEntity.ok().build();

    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal){
        if(principal == null){
            return ResponseEntity.badRequest().build();
        }
        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(user);
    }
    @GetMapping("/me/checkSubscribtion/{channelId}")
    public ResponseEntity<Boolean> checkUserSubscription(Principal principal, @PathVariable Long channelId){
        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Channel channel = channelRepository.findByid(channelId).orElseThrow();
        Optional<Subscription> subscription = subscriptionRepository.findByChannelAndSubscriber(channel,user);

        if(subscription.isPresent()){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }
    }
    @GetMapping("/userSubscriptions")
    public ResponseEntity<?> getUserSubscriptions(Principal principal){
        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Channel> subscribingChannels = subscriptionRepository.findSubscribedChannelsByUserId(user.getId());
        if(subscribingChannels.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subscribingChannels);
    }



    }
