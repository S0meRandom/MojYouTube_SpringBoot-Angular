package org.example.demospringbootangular.ServiceTests;

import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;


    private org.example.demospringbootangular.Service.UserDetailsService userDetailsService;

    private AppUser mockUser;

    @BeforeEach
    void setUp(){
        userDetailsService = new org.example.demospringbootangular.Service.UserDetailsService(userRepository);
        mockUser = new AppUser();
        mockUser.setUsername("kamil");
        mockUser.setPassword("123");
    }

    @Test
    void shouldLoadUserSuccessfully(){
        when(userRepository.findByUsername("kamil")).thenReturn(Optional.of(mockUser));

        UserDetails result = userDetailsService.loadUserByUsername("kamil");

        assertNotNull(result);
        assertEquals("kamil",result.getUsername());
        assertEquals("123",result.getPassword());
        assertTrue(result.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_USER")));
        verify(userRepository, times(1)).findByUsername("kamil");
;    }

}
