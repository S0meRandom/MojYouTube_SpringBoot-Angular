package org.example.demospringbootangular.repository;

import org.example.demospringbootangular.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction,Long> {
    Optional<Reaction> findByVideoIdAndUserId(Long videoId, Long userId);

}
