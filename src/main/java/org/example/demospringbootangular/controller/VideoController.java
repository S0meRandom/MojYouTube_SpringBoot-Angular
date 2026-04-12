package org.example.demospringbootangular.controller;

import jakarta.annotation.Resource;
import org.apache.coyote.Response;
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
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @PostMapping("/upload")
    public Video saveVideo(@RequestParam("videoFile") MultipartFile videoFile,
                           @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
                           @RequestParam("title") String title,
                           @RequestParam("description") String description,Principal principal){
        AppUser currentUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        Channel channel = channelRepository.findByowner(currentUser).orElseThrow();

        Video video = new Video();
        video.setTitle(title);
        videoService.handleVideo_Thumbnail(videoFile,thumbnailFile, video);
        video.setAuthor(currentUser);
        video.setCreationDate(LocalDateTime.now());
        video.setDescription(description);
        video.setChannel(channel);
        return videoRepository.save(video);
    }

        @GetMapping
        public List<Video> getAllVideos(){
            return videoRepository.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> getVideo(@PathVariable long id){
            Video video = videoRepository.findByid(id).orElseThrow();
            return ResponseEntity.ok().body(video);
        }


        @GetMapping("/thumbnail/{id}")
        public ResponseEntity<?> getVideoThumbnail(@PathVariable Long id){
            Video video = videoRepository.findByid(id).orElseThrow();

            try{
                String cleanPath = video.getThumbnailUrl();
                if (cleanPath.startsWith("/")) {
                    cleanPath = cleanPath.substring(1);
                }
                Path path = Paths.get("").toAbsolutePath().resolve(cleanPath);
                UrlResource file = new UrlResource(path.toUri());

                if (file.exists() && file.isReadable()) {
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG)
                            .body(file);
                } else {
                    System.out.println("BŁĄD: Plik nie istnieje pod ścieżką: " + path.toAbsolutePath());
                    return ResponseEntity.notFound().build();
                }
            }catch(Exception e){
                return ResponseEntity.notFound().build();

            }

        }
        @GetMapping("/videoPlay/{id}")
        public ResponseEntity<StreamingResponseBody> getVideoPlay(@PathVariable Long id, @RequestHeader HttpHeaders headers){
            Video video = videoRepository.findByid(id).orElseThrow();

            try{

                String cleanPath = video.getUrl();
                if (cleanPath.startsWith("/")) {
                    cleanPath = cleanPath.substring(1);
                }
                Path path = Paths.get("").toAbsolutePath().resolve(cleanPath);
                File file = path.toFile();

                StreamingResponseBody responseBody = outputStream -> {
                    try(InputStream inputStream = new FileInputStream(file)){
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while((bytesRead=inputStream.read(buffer)) != -1){
                            outputStream.write(buffer,0,bytesRead);
                        }
                        outputStream.flush();
                    }
                };
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\""+file.getName()+"\"")
                        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))
                        .contentType(MediaType.parseMediaType("video/mp4"))
                        .body(responseBody);
            }catch(Exception e){
                return ResponseEntity.internalServerError().build();

            }
        }
        @PutMapping("/{id}")
        public ResponseEntity<?> updateViews(@PathVariable long id){
            Video video = videoRepository.findByid(id).orElseThrow();
            video.setViews(video.getViews()+1);
            videoRepository.save(video);
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
            List<Video> searchedVideos = new ArrayList<>();
            if(query==null || query.isBlank()){
                searchedVideos = videoRepository.findAll();
                return ResponseEntity.ok(searchedVideos);
            }
            searchedVideos = videoRepository.searchVideos(query);
            return ResponseEntity.ok(searchedVideos);
        }
}



