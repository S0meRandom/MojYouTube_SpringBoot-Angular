package org.example.demospringbootangular.repository;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Playlist;
import org.example.demospringbootangular.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayListRepository extends JpaRepository<Playlist,Long> {
    List<Playlist> findByPlaylistCreator(AppUser creator);
}
