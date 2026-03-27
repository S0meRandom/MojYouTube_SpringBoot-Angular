package org.example.demospringbootangular.controller;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Channel;
import org.example.demospringbootangular.repository.ChannelRepository;
import org.example.demospringbootangular.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/channel")
public class channelController {
    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/create")
    public ResponseEntity<?> createNewChannel(@RequestParam Principal principal, @RequestParam String channelName){
        if(channelRepository.findByname(channelName).isPresent()){
            return ResponseEntity.notFound().build();
        }
        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Channel newChannel = new Channel();
        newChannel.setName(channelName);
        newChannel.setOwner(user);
        channelRepository.save(newChannel);

        return ResponseEntity.ok().build();
    };
}
