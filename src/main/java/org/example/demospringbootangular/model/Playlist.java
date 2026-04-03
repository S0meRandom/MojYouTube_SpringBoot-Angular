package org.example.demospringbootangular.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "playlist_creator_id")
    private AppUser playlistCreator;

    private String playlistName;

    @ManyToMany
    @JoinTable(
            name="playlist_videos",
            joinColumns = @JoinColumn(name="playlist_id"),
            inverseJoinColumns = @JoinColumn(name="video_id")
    )
    private List<Video> playlistVideos = new ArrayList<>();

    public AppUser getPlaylistCreator() {
        return playlistCreator;
    }

    public void setPlaylistCreator(AppUser playlistCreator) {
        this.playlistCreator = playlistCreator;
    }
    public String getPlaylistName(){
        return playlistName;
    }
    public void setPlaylistName(String newPlayListName){
        this.playlistName = newPlayListName;
    }
    public List<Video> getPlaylistVideos(){
        return playlistVideos;
    }
    public void setPlaylistVideos(List<Video> newPlaylistVideos){
        this.playlistVideos = newPlaylistVideos;
    }

    public long getId(){
        return id;
    }
    public void setId(long newId){
        this.id = newId;
    }
}
