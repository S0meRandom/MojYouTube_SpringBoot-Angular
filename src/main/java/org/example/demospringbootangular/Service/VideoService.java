package org.example.demospringbootangular.Service;

import jakarta.transaction.Transactional;
import org.example.demospringbootangular.model.*;
import org.example.demospringbootangular.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
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
import java.util.Optional;
import java.util.UUID;

@Service
public class VideoService {

    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;


    @Autowired
    private ChannelService channelService;

    @Autowired
    private PlayListRepository playListRepository;

    @Autowired
    private PlaylistService playlistService;

    @Transactional
    public void saveVideo(MultipartFile videoFile,
                          MultipartFile thumbnailFile,
                          String title,
                          String description,
                          Principal principal){
        AppUser currentUser = userRepository.findByUsername(principal.getName()).orElseThrow();
        Channel channel = channelRepository.findByOwner(currentUser).orElseThrow();

        Video video = new Video();
        video.setTitle(title);
        handleVideo_Thumbnail(videoFile,thumbnailFile, video);
        video.setAuthor(currentUser);
        video.setCreationDate(LocalDateTime.now());
        video.setDescription(description);
        video.setChannel(channel);
        videoRepository.save(video);

    }
    @Transactional
    public void setReaction(Long videoId, AppUser user, ReactionType newType) {
        Optional<Reaction> existingReaction = reactionRepository.findByVideoIdAndUserId(videoId, user.getId());
        Video video = videoRepository.findByid(videoId).orElseThrow();

        if (existingReaction.isPresent()) {
            Reaction reaction = existingReaction.get();

            if (reaction.getReactionType() == newType) {
                if (newType == ReactionType.LIKE) {
                    video.setLikes(Math.max(0, video.getLikes() - 1));
                } else {
                    video.setDislikes(Math.max(0, video.getDislikes() - 1));
                }
                reactionRepository.delete(reaction);
                reactionRepository.delete(reaction);
            } else {
                if (newType == ReactionType.LIKE) {
                    video.setLikes(video.getLikes() + 1);
                    video.setDislikes(video.getDislikes() - 1);
                } else {
                    video.setDislikes(video.getDislikes() + 1);
                    video.setLikes(video.getLikes() - 1);
                }
                reaction.setReactionType(newType);
                reactionRepository.save(reaction);
            }
        } else {
            if(newType == ReactionType.LIKE){
                video.setLikes(video.getLikes()+1);
            }else{
                video.setDislikes(video.getDislikes()+1);
            }

            Reaction reaction = new Reaction();
            reaction.setVideo(videoRepository.findById(videoId).get());
            reaction.setUser(user);
            reaction.setReactionType(newType);
            reactionRepository.save(reaction);
        }
        videoRepository.save(video);
    }
    public List<Video> searchVideos(String query){
        if(query==null || query.trim().isEmpty()){
            return videoRepository.findAll();
        }
        return videoRepository.searchVideos(query);
    }
    @Transactional
    public void handleVideo_Thumbnail(MultipartFile videoFile, MultipartFile thumbnailFile, Video video){
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
            Files.copy(videoFile.getInputStream(),filePath_video, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(thumbnailFile.getInputStream(), filePath_thumbnail, StandardCopyOption.REPLACE_EXISTING);
            video.setUrl("videoFolder/"+folderName+"/"+uniqueFilename_video);
            video.setThumbnailUrl("videoFolder/"+folderName+"/"+uniqueFilename_thumbnail);

        }catch(IOException io){
            throw new RuntimeException();
        }
    }
    @Transactional
    public UrlResource getVideoThumbnail(long videoId) {
        Video video = videoRepository.findByid(videoId).orElseThrow();

        UrlResource file = null;
        try {
            String cleanPath = video.getThumbnailUrl();
            if (cleanPath.startsWith("/")) {
                cleanPath = cleanPath.substring(1);
            }
            Path path = Paths.get("").toAbsolutePath().resolve(cleanPath);
            file = new UrlResource(path.toUri());
        } catch (Exception ignored) {
        }
        return file;
    }
    public StreamingResponseBody getVideoPlay(long videoId) {
        Video video = videoRepository.findByid(videoId).orElseThrow();

        StreamingResponseBody responseBody = null;
        try {

            String cleanPath = video.getUrl();
            if (cleanPath.startsWith("/")) {
                cleanPath = cleanPath.substring(1);
            }
            Path path = Paths.get("").toAbsolutePath().resolve(cleanPath);
            File file = path.toFile();

            responseBody = outputStream -> {
                try (InputStream inputStream = new FileInputStream(file)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                }
            };
        } catch (Exception e) {
        }

        return responseBody;
    }
    public void updateViews(long videoId){
        Video video = videoRepository.findByid(videoId).orElseThrow();
        Channel channel = videoRepository.findChannelByVideoId(videoId);
        AppUser user = channelRepository.findOwnerByChannelId(channel.getId()).orElseThrow();
        Playlist userHistory = playListRepository.findById(user.getUserHistoryPlaylistId()).orElseThrow();
        playlistService.addVideoToPlaylist(videoId,userHistory.getId());
        channelService.updateChannelViews(channel);
        video.setViews(video.getViews()+1);
        videoRepository.save(video);
    }
}
