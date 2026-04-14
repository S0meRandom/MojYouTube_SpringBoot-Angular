package org.example.demospringbootangular.model;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    private String commentContent;

    private long userId;

    private long videoId;

    public String getId(){
        return id;
    }
    public String getCommentContent(){
        return commentContent;
    }
    public void setCommentContent(String newComment){
        this.commentContent = newComment;
    }
    public long getUser_id(){
        return userId;
    }
    public void setUser_id(long newUser_id){
        this.userId = newUser_id;
    }
    public long getVideo_id(){
        return videoId;
    }
    public void setVideo_id(long newVideo_id){
        this.videoId = newVideo_id;
    }
}
