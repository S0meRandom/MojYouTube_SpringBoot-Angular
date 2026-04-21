package org.example.demospringbootangular.controller;

import org.example.demospringbootangular.Service.ChannelService;
import org.example.demospringbootangular.Service.VideoService;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Channel;
import org.example.demospringbootangular.model.ReactionType;
import org.example.demospringbootangular.model.Video;
import org.example.demospringbootangular.repository.ChannelRepository;
import org.example.demospringbootangular.repository.ReactionRepository;
import org.example.demospringbootangular.repository.UserRepository;
import org.example.demospringbootangular.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/video")
public class VideoController {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private VideoService videoService;

    @Autowired
    private ChannelService channelService;

    @PostMapping("/upload")
    public ResponseEntity<?> saveVideo(@RequestParam("videoFile") MultipartFile videoFile,
                           @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
                           @RequestParam("title") String title,
                           @RequestParam("description") String description,Principal principal){
        videoService.saveVideo(videoFile,thumbnailFile,title,description,principal);
        return ResponseEntity.ok().build();
    }

        @GetMapping
        public ResponseEntity<?> getAllVideos(){
            return ResponseEntity.ok(videoRepository.findAll());
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> getVideo(@PathVariable long id){
            Video video = videoRepository.findByid(id).orElseThrow();
            return ResponseEntity.ok().body(video);
        }


        @GetMapping("/thumbnail/{id}")
        public ResponseEntity<?> getVideoThumbnail(@PathVariable Long id){
            UrlResource file = videoService.getVideoThumbnail(id);
            if(file.exists() && file.isReadable()){
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
            }else{
                return ResponseEntity.notFound().build();
            }


        }
        @GetMapping("/videoPlay/{id}")
        public ResponseEntity<StreamingResponseBody> getVideoPlay(@PathVariable Long id, @RequestHeader HttpHeaders headers){
            StreamingResponseBody responseBody = videoService.getVideoPlay(id);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("video/mp4"))
                        .body(responseBody);
        }
        @PutMapping("/{id}")
        public ResponseEntity<?> updateViews(@PathVariable long id){
            videoService.updateViews(id);
            return ResponseEntity.ok().build();
        }

        @PostMapping("/react/{id}")
        public ResponseEntity<?> handleReaction(@PathVariable long id,
                                                @RequestParam ReactionType type,
                                                Principal principal){
            AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow();
            videoService.setReaction(id,user,type);
            return ResponseEntity.ok().build();

        }

        @GetMapping("/likedVideos")
        public ResponseEntity<?> getUserLikedVideos(Principal principal){
            AppUser loggedUser = userRepository.findByUsername(principal.getName()).orElseThrow();
            List<Video> likedVideos = reactionRepository.findLikedVideosByUserId(loggedUser.getId());
            if(likedVideos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(likedVideos);

        }

        @GetMapping("/searchVideos")
        public ResponseEntity<?> searchVideos(@RequestParam(required = false) String query){
            List<Video> searchedVideos = videoService.searchVideos(query);
            return ResponseEntity.ok(searchedVideos);
        }
}



