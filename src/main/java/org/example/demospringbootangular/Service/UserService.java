package org.example.demospringbootangular.Service;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Channel;
import org.example.demospringbootangular.model.RegistrationDTO;
import org.example.demospringbootangular.repository.ChannelRepository;
import org.example.demospringbootangular.repository.PlayListRepository;
import org.example.demospringbootangular.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private PlayListRepository playListRepository;

    @Autowired
    private HistoryService historyService;


    public void registerUser(RegistrationDTO dto){
        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        historyService.createUserHistory(user);
        userRepository.save(user);

        Channel channel = new Channel();
        channel.setName(dto.getChannelName());
        channel.setOwner(user);
        channelRepository.save(channel);
    }

}
