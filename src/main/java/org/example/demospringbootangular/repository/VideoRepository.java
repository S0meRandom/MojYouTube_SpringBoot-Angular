package org.example.demospringbootangular.repository;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video,Long> {
    Optional<Video> findByid(Long id);
    List<Video> findByauthor(AppUser user);
}
