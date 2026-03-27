package org.example.demospringbootangular.model;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Table(name="channels")
public class Channel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "subscribers")
    private long subscribers = 0L;
    @Column(name="channelName")
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private AppUser owner;

    private String country = "notSpecified";

    private long sumViews = 0;

    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser owner) {
        this.owner = owner;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public long getSubscribers(){
        return subscribers;
    }
    public void setSubscribers(Long newSubsribers){
        this.subscribers = newSubsribers;
    }
    public String getName(){
        return name;
    }
    public void setName(String newName){
        this.name = newName;
    }
    public String getCountry(){
        return country;
    }
    public void setCountry(String newCountry){
        this.country = newCountry;
    }

    public Long getSumViews(){
        return sumViews;
    }
    public void setSumViews(Long newSumViews){
        this.sumViews = newSumViews;
    }
}
