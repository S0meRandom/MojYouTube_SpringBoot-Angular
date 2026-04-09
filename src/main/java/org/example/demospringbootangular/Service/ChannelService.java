package org.example.demospringbootangular.Service;

import jakarta.transaction.Transactional;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Channel;
import org.example.demospringbootangular.model.Subscription;
import org.example.demospringbootangular.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChannelService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Transactional
    public void subscribeOrUnsubscribe(Channel channel, AppUser user){
        Optional<Subscription> subscription = subscriptionRepository.findByChannelAndSubscriber(channel,user);

        if(subscription.isPresent()){
            Subscription existingSubscribtion = subscription.get();
            subscriptionRepository.decrementSubscribers(existingSubscribtion.getId());
        }else{
            Subscription newSub = new Subscription();
            newSub.setChannel(channel);
            newSub.setSubscriber(user);
            subscriptionRepository.incrementSubscribers(channel.getId());
            subscriptionRepository.save(newSub);

        }


    }
}
