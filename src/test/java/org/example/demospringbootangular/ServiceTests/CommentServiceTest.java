package org.example.demospringbootangular.ServiceTests;

import org.example.demospringbootangular.Service.CommentService;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Comment;
import org.example.demospringbootangular.repository.CommentRepository;
import org.example.demospringbootangular.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void shouldCreateCommentSuccessfully(){
        String content = "testowy content";
        Long videoId = 100L;

        AppUser commenter = new AppUser();
        commenter.setId(1L);
        commenter.setUsername("testowy username");

        commentService.createComment(content,videoId,commenter);

        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);

        verify(commentRepository).save(commentCaptor.capture());

        Comment savedComment = commentCaptor.getValue();

        assertEquals(content, savedComment.getCommentContent());
        assertEquals(1L, savedComment.getUser_id());
        assertEquals(videoId, savedComment.getVideo_id());


    }
}
