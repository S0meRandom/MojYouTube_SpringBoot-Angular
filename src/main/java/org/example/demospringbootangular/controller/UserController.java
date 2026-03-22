package org.example.demospringbootangular.controller;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AppUser user){
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            return ResponseEntity.badRequest().body("Użytkownik już istnieje");

        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Zarejestrowano pomyślnie");

    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal){
        if(principal == null){
            return ResponseEntity.badRequest().build();
        }
        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow();
        System.out.println("Odebrano użytkownika: " + user.getUsername());
        return ResponseEntity.ok(user);
    }
}
