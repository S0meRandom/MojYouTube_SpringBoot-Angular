package org.example.demospringbootangular.repository;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel,Long> {
    Optional<Channel> findByid(Long id);
    Optional<Channel> findByName(String channelName);
    Optional<Channel> findByOwner(AppUser owner);


    @Query("SELECT r.owner FROM Channel r WHERE r.id = :channelId")
    Optional<AppUser> findOwnerByChannelId(long channelId);

}
