package org.example.demospringbootangular.model;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="userNotifications")
public class Notification {
    @Id
    private String id;

    private String userId;

    private String content;

    private Boolean isRead = false;

    public String getId(){
        return id;
    }
    public String getUserId(){
        return userId;
    }
    public void setUserId(String newUserId){
        this.userId = newUserId;
    }
    public String getContent(){
        return content;
    }
    public void setContent(String newContent){
        this.content = newContent;
    }
    public Boolean getIsRead(){
        return isRead;
    }
    public void setIsRead(Boolean newIsRead){
        this.isRead = newIsRead;
    }
}
