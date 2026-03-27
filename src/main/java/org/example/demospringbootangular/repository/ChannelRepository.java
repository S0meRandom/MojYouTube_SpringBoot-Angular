package org.example.demospringbootangular.repository;

import org.example.demospringbootangular.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel,Long> {
    Optional<Channel> findByid(Long id);
    Optional<Channel> findByname(String channelName);


}
