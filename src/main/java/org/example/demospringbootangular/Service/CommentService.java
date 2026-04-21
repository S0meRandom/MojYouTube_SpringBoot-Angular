package org.example.demospringbootangular.Service;

import jakarta.transaction.Transactional;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Comment;
import org.example.demospringbootangular.repository.CommentRepository;
import org.example.demospringbootangular.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createComment(String commentContent, Long video_id,AppUser commenter){
        Comment newComment = new Comment();
        newComment.setCommentContent(commentContent);
        newComment.setUser_id(commenter.getId());
        newComment.setVideo_id(video_id);
        commentRepository.save(newComment);
    }
    @Transactional
    public  List<Map<String, Object>> getVideoComments(long id){
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
        return finalData;

    }

}


