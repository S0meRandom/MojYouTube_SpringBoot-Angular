package org.example.demospringbootangular.controller;

import org.apache.coyote.Response;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Playlist;
import org.example.demospringbootangular.model.Video;
import org.example.demospringbootangular.repository.PlayListRepository;
import org.example.demospringbootangular.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlayListController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayListRepository playListRepository;

    @GetMapping("/loggerUserPlaylists")
    public ResponseEntity<?> getUserPlaylists(Principal principal){
        AppUser loggedUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Playlist> userPlaylists = playListRepository.findByPlaylistCreator(loggedUser);
        if(userPlaylists.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(userPlaylists);
    }
    @PostMapping
    public ResponseEntity<?> createNewPlaylist(Principal principal,@RequestBody String playlistName){
        AppUser playlistCreator = userRepository.findByUsername(principal.getName()).orElseThrow();
        Playlist newPlaylist = new Playlist();
        newPlaylist.setPlaylistCreator(playlistCreator);
        newPlaylist.setPlaylistName(playlistName);
        playListRepository.save(newPlaylist);

        return ResponseEntity.ok().build();

    }
}
