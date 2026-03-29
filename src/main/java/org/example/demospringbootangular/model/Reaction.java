package org.example.demospringbootangular.model;

import jakarta.persistence.*;

@Entity
@Table(name="video_reactions",uniqueConstraints = @UniqueConstraint(columnNames={"user_id","video_id"}))
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name="video_id")
    private Video video;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    public long getId(){
        return id;
    }
    public void setId(long newId){
        this.id = newId;
    }
    public AppUser getUser(){
        return user;
    }
    public void setUser(AppUser newUser){
        this.user = newUser;
    }
    public Video getVideo(){
        return video;
    }
    public void setVideo(Video newVideo){
        this.video = newVideo;
    }
    public ReactionType getReactionType(){
        return type;
    }
    public void setReactionType(ReactionType newType){
        this.type = newType;
    }
}
