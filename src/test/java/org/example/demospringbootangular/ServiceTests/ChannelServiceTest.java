package org.example.demospringbootangular.ServiceTests;

import org.example.demospringbootangular.Service.ChannelService;
import org.example.demospringbootangular.model.AppUser;
import org.example.demospringbootangular.model.Channel;
import org.example.demospringbootangular.model.Subscription;
import org.example.demospringbootangular.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private ChannelService channelService;

    private Channel channel;
    private AppUser user;

    @BeforeEach
    void setUp() {
        channel = new Channel();
        channel.setId(1L);

        user = new AppUser();
        user.setId(2L);
    }


    @Test
    void shouldUnsubscribeWhenSubscriptionExists() {

        Subscription existingSub = new Subscription();
        existingSub.setId(10L);


        when(subscriptionRepository.findByChannelAndSubscriber(channel, user))
                .thenReturn(Optional.of(existingSub));

        channelService.subscribeOrUnsubscribe(channel, user);

        verify(subscriptionRepository).decrementSubscribers(10L);

        verify(subscriptionRepository, never()).incrementSubscribers(any());
        verify(subscriptionRepository, never()).save(any());
    }


    @Test
    void shouldSubscribeWhenSubscriptionDoesNotExist() {

        when(subscriptionRepository.findByChannelAndSubscriber(channel, user))
                .thenReturn(Optional.empty());


        channelService.subscribeOrUnsubscribe(channel, user);

        verify(subscriptionRepository).incrementSubscribers(channel.getId());
        verify(subscriptionRepository).save(any(Subscription.class));
        verify(subscriptionRepository, never()).decrementSubscribers(any());
    }
}