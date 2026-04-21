package org.example.demospringbootangular.controller;

import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.example.demospringbootangular.Service.PlaylistService;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Playlist;
import org.example.demospringbootangular.model.Video;
import org.example.demospringbootangular.repository.PlayListRepository;
import org.example.demospringbootangular.repository.UserRepository;
import org.example.demospringbootangular.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlayListController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayListRepository playlistRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/loggerUserPlaylists")
    public ResponseEntity<?> getUserPlaylists(Principal principal){
        AppUser loggedUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Playlist> userPlaylists = playlistRepository.findByPlaylistCreator(loggedUser);
        if(userPlaylists.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(userPlaylists);
    }
    @GetMapping("/playlistData/{id}")
    public ResponseEntity<?> getPlaylistInfo(@PathVariable long id){
        Playlist selectedPlaylist = playlistRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(selectedPlaylist);
    }
    @GetMapping("/{playlistId}")
    public ResponseEntity<?> getPlaylistVideos(@PathVariable long playlistId){
        Playlist selectedPlaylist = playlistRepository.findById(playlistId).orElseThrow();
        List<Video> playlistVideos = selectedPlaylist.getPlaylistVideos();
        if(playlistVideos.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(playlistVideos);
        }
    }

    @PostMapping
    public ResponseEntity<?> createNewPlaylist(Principal principal,@RequestBody String playlistName){
        AppUser playlistCreator = userRepository.findByUsername(principal.getName()).orElseThrow();
        playlistService.createNewPlaylist(playlistCreator,playlistName);
        return ResponseEntity.ok().build();

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable long id){
        Playlist selectedPlaylist = playlistRepository.findById(id).orElseThrow();
        playlistRepository.delete(selectedPlaylist);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/addToPlaylist/{videoId}/{playlistId}")
    @Transactional
    public ResponseEntity<?> addVideoToPlaylist(@PathVariable long videoId,@PathVariable long playlistId){
        playlistService.addVideoToPlaylist(videoId,playlistId);
        return ResponseEntity.ok().build();

    }
}
