package org.example.demospringbootangular.Service;

import jakarta.transaction.Transactional;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Reaction;
import org.example.demospringbootangular.model.ReactionType;
import org.example.demospringbootangular.repository.ReactionRepository;
import org.example.demospringbootangular.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private VideoRepository videoRepository;


    @Transactional
    public void setReaction(Long videoId, AppUser user, ReactionType newType) {
        Optional<Reaction> existingReaction = reactionRepository.findByVideoIdAndUserId(videoId, user.getId());

        if (existingReaction.isPresent()) {
            Reaction reaction = existingReaction.get();

            if (reaction.getReactionType() == newType) {
                reactionRepository.delete(reaction);
            } else {

                reaction.setReactionType(newType);
                reactionRepository.save(reaction);
            }
        } else {

            Reaction reaction = new Reaction();
            reaction.setVideo(videoRepository.findById(videoId).get());
            reaction.setUser(user);
            reaction.setReactionType(newType);
            reactionRepository.save(reaction);
        }
    }
}
