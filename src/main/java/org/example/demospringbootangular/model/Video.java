package org.example.demospringbootangular.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Table(name="videos")
@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name="author_id")
    private AppUser author;


    private LocalDateTime creationDate;
    private String title;
    private String url;
    private String thumbnailUrl;
    private String description;
    @ColumnDefault("0")
    private long views = 0;

    @ColumnDefault("0")
    private long likes = 0;

    @ColumnDefault("0")
    private long dislikes = 0;

    public Long getId(){
        return id;
    }
    public void setId(Long newId){
        this.id = newId;
    }
    public LocalDateTime getCreationDate(){
        return creationDate;
    }
    public void setCreationDate(LocalDateTime newCreationDate){
        this.creationDate = newCreationDate;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String newTitle){
        this.title = newTitle;
    }
    public String getUrl(){
        return url;
    }
    public void setUrl(String newUrl){
        this.url = newUrl;
    }
    public String getThumbnailUrl(){
        return thumbnailUrl;
    }
    public void setThumbnailUrl(String newThumbnailUrl){
        this.thumbnailUrl = newThumbnailUrl;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String newDescription){
        this.description = newDescription;
    }
    public AppUser getAuthor(){
        return author;
    }
    public void setAuthor(AppUser newAuthor){
        this.author = newAuthor;
    }
    public long getLikes(){
        return likes;
    }
    public void setLikes(Long newLikes){
        this.likes = newLikes;
    }
    public Long getDislikes(){
        return dislikes;
    }
    public void setDislikes(Long newDislikes){
        this.dislikes = newDislikes;
    }
    public Long getViews(){
        return views;
    }
    public void setViews(Long newViews){
        this.views = newViews;
    }
}
