package org.example.demospringbootangular.repository;

import org.example.demospringbootangular.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment,String> {
    List<Comment> findByVideoIdAndUserId(Long videoId, Long userId);

    List<Comment> findByVideoId(Long videoId);
}
