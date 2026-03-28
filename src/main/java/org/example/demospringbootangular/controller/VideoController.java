package org.example.demospringbootangular.controller;

import jakarta.annotation.Resource;
import org.apache.coyote.Response;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Channel;
import org.example.demospringbootangular.model.Video;
import org.example.demospringbootangular.repository.ChannelRepository;
import org.example.demospringbootangular.repository.UserRepository;
import org.example.demospringbootangular.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
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
import java.time.LocalDateTime;
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

    @PostMapping("/upload")
    public Video saveVideo(@RequestParam("videoFile") MultipartFile videoFile,
                           @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
                           @RequestParam("title") String title,
                           @RequestParam("description") String description,Principal principal){
        AppUser currentUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        Channel channel = channelRepository.findByowner(currentUser).orElseThrow();

        Video video = new Video();
        video.setTitle(title);
        handleVideo_Thumbnail(videoFile,thumbnailFile, video);
        video.setAuthor(currentUser);
        video.setCreationDate(LocalDateTime.now());
        video.setDescription(description);
        video.setChannel(channel);
        return videoRepository.save(video);
    }
    public void handleVideo_Thumbnail(MultipartFile videoFile,MultipartFile thumbnailFile, Video video){
        if (videoFile == null || videoFile.isEmpty() || thumbnailFile == null || thumbnailFile.isEmpty()) {
            throw new RuntimeException("Brakuje pliku wideo lub miniaturki!");
        }
            try{
                String folderName = video.getTitle()
                        .strip()
                        .toLowerCase()
                        .replaceAll("[^a-z0-9]", "-")
                        .replaceAll("-+", "-")
                        + "-" + UUID.randomUUID().toString().substring(0, 8);
                String directoryPath = "videoFolder";
                Path uploadPath = Paths.get(directoryPath);
                Path childPath = uploadPath.resolve(folderName);

                if(!Files.exists(childPath)){
                    Files.createDirectories(childPath);
                }
                String originalFileName_video = videoFile.getOriginalFilename();
                String originalFileName_thumbnail = thumbnailFile.getOriginalFilename();

                String fileExtensionVideo = "";
                String fileExtensionThumbnail = "";

                assert originalFileName_thumbnail != null;
                fileExtensionThumbnail = originalFileName_thumbnail.substring(originalFileName_thumbnail.lastIndexOf("."));


                if(originalFileName_video != null && originalFileName_video.contains(".")){
                    fileExtensionVideo = originalFileName_video.substring(originalFileName_video.lastIndexOf("."));
                }
                String uniqueFilename_thumbnail = UUID.randomUUID().toString()+fileExtensionThumbnail;
                String uniqueFilename_video = UUID.randomUUID().toString() + fileExtensionVideo;
                Path filePath_video = childPath.resolve(uniqueFilename_video);
                Path filePath_thumbnail = childPath.resolve(uniqueFilename_thumbnail);
                Files.copy(videoFile.getInputStream(),filePath_video,StandardCopyOption.REPLACE_EXISTING);
                Files.copy(thumbnailFile.getInputStream(), filePath_thumbnail, StandardCopyOption.REPLACE_EXISTING);
                video.setUrl("videoFolder/"+folderName+"/"+uniqueFilename_video);
                video.setThumbnailUrl("videoFolder/"+folderName+"/"+uniqueFilename_thumbnail);

            }catch(IOException io){
                throw new RuntimeException();
            }
        }


        @GetMapping
        public List<Video> getAllVideos(){
            return videoRepository.findAll();
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
    private ResourceRegion resourceRegion(UrlResource video, HttpHeaders headers) throws Exception {
        long contentLength = video.contentLength();
        HttpRange range = headers.getRange().isEmpty() ? null : headers.getRange().get(0);
        if (range != null) {
            long start = range.getRangeStart(contentLength);
            long end = range.getRangeEnd(contentLength);
            long rangeLength = Math.min(1024 * 1024, end - start + 1);
            return new ResourceRegion(video, start, rangeLength);
        } else {
            long rangeLength = Math.min(1024 * 1024, contentLength);
            return new ResourceRegion(video, 0, rangeLength);
        }
    }

}



