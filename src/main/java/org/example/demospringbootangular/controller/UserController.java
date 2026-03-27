package org.example.demospringbootangular.controller;

import jakarta.transaction.Transactional;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Channel;
import org.example.demospringbootangular.model.RegistrationDTO;
import org.example.demospringbootangular.repository.ChannelRepository;
import org.example.demospringbootangular.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ChannelRepository channelRepository;



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
        System.out.println("Odebrano użytkownika: " + user.getUsername());
        return ResponseEntity.ok(user);
    }
}
