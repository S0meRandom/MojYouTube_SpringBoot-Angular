package org.example.demospringbootangular.Service;

import jakarta.transaction.Transactional;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Playlist;
import org.example.demospringbootangular.repository.PlayListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    @Autowired
    private PlayListRepository playlistRepository;
    @Transactional
    public void createUserHistory(AppUser user){
        Playlist history = new Playlist();
        history.setPlaylistCreator(user);
        history.setPlaylistName("Historia");
        Playlist savedHistory = playlistRepository.save(history);
        user.setUserHistoryPlaylistId(savedHistory.getId());



    }


}
