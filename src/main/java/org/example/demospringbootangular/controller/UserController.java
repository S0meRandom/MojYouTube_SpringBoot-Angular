package org.example.demospringbootangular.controller;

import jakarta.transaction.Transactional;
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



    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDTO dto){

        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);

        Channel channel = new Channel();
        channel.setName(dto.getChannelName());
        channel.setOwner(user);
        channelRepository.save(channel);

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
    public ResponseEntity<Boolean> checkUserSubscribtion(Principal principal,@PathVariable Long channelId){
        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Channel channel = channelRepository.findByid(channelId).orElseThrow();
        Optional<Subscription> subscription = subscriptionRepository.findByChannelAndSubscriber(channel,user);

        if(subscription.isPresent()){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }
    }



    }
