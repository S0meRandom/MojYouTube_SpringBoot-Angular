package org.example.demospringbootangular.controller;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Video;
import org.example.demospringbootangular.repository.UserRepository;
import org.example.demospringbootangular.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/video")
public class VideoController {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public Video saveVideo(@RequestParam("videoFile") MultipartFile videoFile,
                           @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
                           @RequestParam("title") String title,
                           @RequestParam("description") String description,Principal principal){
        AppUser currentUser = userRepository.findByUsername(principal.getName()).orElseThrow();

        Video video = new Video();
        video.setTitle(title);
        handleVideo_Thumbnail(videoFile,thumbnailFile, video);
        video.setAuthor(currentUser);
        video.setCreationDate(LocalDateTime.now());
        video.setDescription(description);
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
                video.setUrl("/videoFolder/"+folderName+"/"+uniqueFilename_video);
                video.setThumbnailUrl("/videoFolder/"+folderName+"/"+uniqueFilename_thumbnail);

            }catch(IOException io){
                throw new RuntimeException();
            }
        }
}



