package org.example.demospringbootangular.repository;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Channel;
import org.example.demospringbootangular.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    Optional<Subscription> findByChannelAndSubscriber(Channel channel, AppUser subscriber);

    @Modifying
    @Query("Update Channel c SET c.subscribers = c.subscribers+1 WHERE c.id = :id")
    void incrementSubscribers(Long id);

    @Modifying
    @Query("Update Channel c SET c.subscribers = c.subscribers-1 WHERE c.id = :id")
    void decrementSubscribers(Long id);

    List<Subscription> findBySubscriber(AppUser user);
    @Query("SELECT r.channel FROM Subscription r WHERE r.subscriber.id = :userId ")
    List<Channel> findSubscribedChannelsByUserId(Long userId);

}
