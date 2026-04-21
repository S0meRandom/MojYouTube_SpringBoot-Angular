package org.example.demospringbootangular.Service;

import jakarta.transaction.Transactional;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Playlist;
import org.example.demospringbootangular.model.Video;
import org.example.demospringbootangular.repository.PlayListRepository;
import org.example.demospringbootangular.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistService {

    @Autowired
    private PlayListRepository playlistRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Transactional
    public void createNewPlaylist(AppUser creator,String playlistName){
        Playlist newPlaylist = new Playlist();
        newPlaylist.setPlaylistCreator(creator);
        newPlaylist.setPlaylistName(playlistName);
        playlistRepository.save(newPlaylist);
    }
    @Transactional
    public void addVideoToPlaylist(long videoId,long playlistId){
        Video selectedVideo = videoRepository.findByid(videoId).orElseThrow();
        Playlist selectedPlaylist = playlistRepository.findById(playlistId).orElseThrow();
        selectedPlaylist.getPlaylistVideos().add(selectedVideo);
        playlistRepository.save(selectedPlaylist);
    }


}
