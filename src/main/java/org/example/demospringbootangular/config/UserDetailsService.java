package org.example.demospringbootangular.config;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        AppUser user = userRepository.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("Użytkownik nie istnieje"));

        return User.withUsername(user.getUsername()).password(user.getPassword()).roles("USER").build();
    }
}
