package org.example.demospringbootangular.Service;

import jakarta.transaction.Transactional;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Comment;
import org.example.demospringbootangular.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.Map;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public void createComment(String commentContent, Long video_id,AppUser commenter){
        Comment newComment = new Comment();
        newComment.setCommentContent(commentContent);
        newComment.setUser_id(commenter.getId());
        newComment.setVideo_id(video_id);
        commentRepository.save(newComment);
    }

}


