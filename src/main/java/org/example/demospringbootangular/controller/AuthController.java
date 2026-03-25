package org.example.demospringbootangular.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.example.demospringbootangular.JWT.JwtUtil;
import org.example.demospringbootangular.JWT.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

   @Autowired
    private AuthenticationManager authenticationManager;

   @PostMapping("/login")
   public ResponseEntity<?> authUser(@RequestParam String username, @RequestParam String password){
       Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(username, password));
       SecurityContextHolder.getContext().setAuthentication(authentication);

       String jwt = jwtUtil.generateJwtToken(authentication.getName());
       ResponseCookie responseCookie = ResponseCookie.from("jwt-token",jwt)
               .path("/")
               .maxAge(24*24*60)
               .httpOnly(true)
               .secure(false)
               .sameSite("Lax")
               .build();

       return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,responseCookie.toString()).body("Logowanie udane.");
   }
   @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(){
       ResponseCookie responseCookie = ResponseCookie.from("jwt-token","")
               .path("/")
               .maxAge(0)
               .build();

       return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,responseCookie.toString()).body("Wylogowano pomyślnie");
   }



}
