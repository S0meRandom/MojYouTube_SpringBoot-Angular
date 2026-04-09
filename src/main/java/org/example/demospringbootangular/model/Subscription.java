package org.example.demospringbootangular.model;

import jakarta.persistence.*;

@Entity
@Table(name="subscriptions",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "channel_id"})})
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private AppUser subscriber;

    @ManyToOne
    @JoinColumn(name="channel_id")
    private Channel channel;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public AppUser getSubscriber() {
        return subscriber;
    }
    public void setSubscriber(AppUser newSubscriber){
        this.subscriber = newSubscriber;
    }
    public Channel getChannel(){
        return channel;
    }
    public void setChannel(Channel newChannel){
        this.channel = newChannel;
    }
}
