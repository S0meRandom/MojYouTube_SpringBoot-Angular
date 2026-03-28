package org.example.demospringbootangular.controller;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Channel;
import org.example.demospringbootangular.model.Video;
import org.example.demospringbootangular.repository.ChannelRepository;
import org.example.demospringbootangular.repository.UserRepository;
import org.example.demospringbootangular.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/channel")
public class channelController {
    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideoRepository videoRepository;


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
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserChannel(@PathVariable Long id){
        AppUser loggedUser = userRepository.findById(id).orElseThrow();
        Channel channel = channelRepository.findByowner(loggedUser).orElseThrow();

        return ResponseEntity.ok().body(channel);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> subscribeToChannel(@PathVariable Long id){
        Channel channel = channelRepository.findByid(id).orElseThrow();
        channel.setSubscribers(channel.getSubscribers()+1);
        channelRepository.save(channel);

        return ResponseEntity.ok(channel.getSubscribers());
    }
    @GetMapping("/channelVideos/{id}")
    public ResponseEntity<?> getChannelVideos(@PathVariable Long id){
        Channel channel = channelRepository.findByid(id).orElseThrow();
        List<Video> channelVideos = videoRepository.findBychannel(channel);

        return ResponseEntity.ok().body(channelVideos);

    };

}
