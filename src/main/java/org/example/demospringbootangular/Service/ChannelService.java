package org.example.demospringbootangular.Service;

import jakarta.transaction.Transactional;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Channel;
import org.example.demospringbootangular.model.Comment;
import org.example.demospringbootangular.model.Subscription;
import org.example.demospringbootangular.repository.ChannelRepository;
import org.example.demospringbootangular.repository.CommentRepository;
import org.example.demospringbootangular.repository.SubscriptionRepository;
import org.example.demospringbootangular.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChannelService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void subscribeOrUnsubscribe(Channel channel, AppUser user){
        Optional<Subscription> subscription = subscriptionRepository.findByChannelAndSubscriber(channel,user);

        if(subscription.isPresent()){
            Subscription existingSubscription = subscription.get();
            subscriptionRepository.decrementSubscribers(existingSubscription.getId());
        }else{
            Subscription newSub = new Subscription();
            newSub.setChannel(channel);
            newSub.setSubscriber(user);
            subscriptionRepository.incrementSubscribers(channel.getId());
            subscriptionRepository.save(newSub);

        }


    }
    @Transactional
    public void updateChannelViews(Channel channel){
        channel.setSumViews(channel.getSumViews()+1);
        channelRepository.save(channel);
    }
    @Transactional
    public void createNewChannel(String channelName,AppUser owner){
        Channel newChannel = new Channel();
        newChannel.setName(channelName);
        newChannel.setOwner(owner);
        channelRepository.save(newChannel);
    }
    @Transactional
    public void editChannelInfo(AppUser channelOwner, Map<String,String> newChannelBody){
        String newChannelName = newChannelBody.get("newChannelName");
        String newChannelCountry = newChannelBody.get("newChannelCountry");
        String newChannelDescription = newChannelBody.get("newChannelDescription");

        Channel ownersChannel = channelRepository.findByOwner(channelOwner).orElseThrow();
        ownersChannel.setName(newChannelName);
        ownersChannel.setCountry(newChannelCountry);
        ownersChannel.setDescription(newChannelDescription);
        channelRepository.save(ownersChannel);

    }

}
