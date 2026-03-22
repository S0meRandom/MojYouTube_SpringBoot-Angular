package org.example.demospringbootangular.model;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class AppUser {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true,nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(unique=true,nullable=false)
    private String email;

    public Long getId(){
        return id;
    }
    public void setId(Long newId){
        this.id = newId;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String newUsername){
        this.username = newUsername;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String newEmail){
        this.email = newEmail;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String newPassword){
        this.password = newPassword;
    }
}
