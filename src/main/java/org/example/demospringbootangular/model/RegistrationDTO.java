package org.example.demospringbootangular.model;

public class RegistrationDTO {

    private String username;
    private String password;
    private String email;
    private String channelName;

    public String getUsername(){
        return username;

    }
    public void setUsername(String newUsername){
        this.username = newUsername;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String newPassword){
        this.password = newPassword;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String newEmail){
        this.email = newEmail;
    }
    public String getChannelName(){
        return channelName;
    }
    public void setChannelName(String newChannelName){
        this.channelName = newChannelName;
    }
}
