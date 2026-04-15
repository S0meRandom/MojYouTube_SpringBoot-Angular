package org.example.demospringbootangular.controller;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Comment;
import org.example.demospringbootangular.repository.CommentRepository;
import org.example.demospringbootangular.repository.UserRepository;
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

    @PostMapping("/create/{id}")
    public void createComment(@RequestBody Map<String,String> content, @PathVariable("id") Long video_id, Principal principal){
        Comment newComment = new Comment();
        String commentContent = content.get("content");
        AppUser commenter = userRepository.findByUsername(principal.getName()).orElseThrow();
        newComment.setCommentContent(commentContent);
        newComment.setUser_id(commenter.getId());
        newComment.setVideo_id(video_id);
        commentRepository.save(newComment);

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
