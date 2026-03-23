package org.example.demospringbootangular.config;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Video;
import org.example.demospringbootangular.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    public Video saveVideo(MultipartFile file, String title, AppUser author){
        saveToDisk(file);



        Video video = new Video();
        video.setTitle(title);
        video.setAuthor(author);
        video.setCreationDate(LocalDateTime.now());
        return videoRepository.save(video);
    }
    public void saveToDisk(MultipartFile file){
        if(file != null && file.isEmpty()){
            try{
                String directoryPath = "/videoFolder";
                Path uploadPath = Paths.get(directoryPath);

                if(!Files.exists(uploadPath)){
                    Files.createDirectories(uploadPath);
                }
                String originalFileName = file.getOriginalFilename();
                String fileExtension = "";

                if(originalFileName != null && originalFileName.contains(".")){
                    fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                }
                String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
                Path filePath = uploadPath.resolve(uniqueFilename);
                Files.copy(file.getInputStream(),filePath,StandardCopyOption.REPLACE_EXISTING);

            }catch(IOException io){
                throw new RuntimeException();
            }
        }
    }

}
