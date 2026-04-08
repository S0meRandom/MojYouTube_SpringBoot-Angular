package org.example.demospringbootangular.repository;

import org.example.demospringbootangular.model.Reaction;
import org.example.demospringbootangular.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction,Long> {
    Optional<Reaction> findByVideoIdAndUserId(Long videoId, Long userId);

    @Query("SELECT r.video FROM Reaction r WHERE r.user.id = :userId AND r.type = 'LIKE'")
    List<Video> findLikedVideosByUserId(@Param("userId") Long userId);

}
