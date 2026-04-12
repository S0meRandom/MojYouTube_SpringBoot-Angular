package org.example.demospringbootangular.repository;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Channel;
import org.example.demospringbootangular.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface VideoRepository extends JpaRepository<Video,Long> {
    Optional<Video> findByid(Long id);
    List<Video> findByauthor(AppUser user);
    List<Video> findBychannel(Channel channel);
    @Query(value = "SELECT * FROM videos WHERE title % :query ORDER BY similarity(title, :query) DESC", nativeQuery = true)
    List<Video> searchVideos(@Param("query") String query);
}
