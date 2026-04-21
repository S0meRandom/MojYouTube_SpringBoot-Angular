package org.example.demospringbootangular.controller;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Playlist;
import org.example.demospringbootangular.repository.PlayListRepository;
import org.example.demospringbootangular.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayListRepository playlistRepository;

    @GetMapping("/userHistory")
    public ResponseEntity<?> getUserHistory(Principal principal){
        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow();
        Playlist userHistory = playlistRepository.findById(user.getUserHistoryPlaylistId()).orElseThrow();
        return ResponseEntity.ok(userHistory);
    }




}
