package org.example.demospringbootangular.controller;

import org.example.demospringbootangular.Service.CommentService;
import org.example.demospringbootangular.Service.NotificationService;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Comment;
import org.example.demospringbootangular.model.Notification;
import org.example.demospringbootangular.model.Video;
import org.example.demospringbootangular.repository.CommentRepository;
import org.example.demospringbootangular.repository.NotificationRepository;
import org.example.demospringbootangular.repository.UserRepository;
import org.example.demospringbootangular.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping("/create/{id}")
    public void createComment(@RequestBody Map<String,String> content, @PathVariable("id") Long video_id, Principal principal){
        String commentContent = content.get("content");
        AppUser commenter = userRepository.findByUsername(principal.getName()).orElseThrow();

        commentService.createComment(commentContent,video_id,commenter);

        Video video = videoRepository.findByid(video_id).orElseThrow();
        AppUser videoCreator = video.getAuthor();
        String videoName = video.getTitle();

        Notification newNotification = notificationService.newCommentNotification(videoName,commenter.getUsername(),videoCreator);
        notificationRepository.save(newNotification);

    }
    @GetMapping("/getVideoComments/{id}")
    public ResponseEntity<?> getVideoComments(@PathVariable Long id){
        List<Comment> videoComments = commentRepository.findByVideoId(id);
        Set<Long> userIds = videoComments.stream().map(Comment::getUser_id).collect(Collectors.toSet());
        List<AppUser> appUsers = userRepository.findAllById(userIds);
        Map<Long, AppUser> userLookup = appUsers.stream().collect(Collectors.toMap(AppUser::getId,u->u));

        List<Map<String, Object>> finalData = videoComments.stream().map(comment -> {
            Map<String,Object> box = new HashMap<>();
            box.put("comment",comment);
            AppUser author = userLookup.get(comment.getUser_id());
            box.put("author",author);
            return box;

        }).toList();
        return ResponseEntity.ok(finalData);

    }
}
