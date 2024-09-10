package ru.practicum.ewm.subscription;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionMapper {
    public static Subscription mapToSubscription(SubscriptionDto dto, User subscriber, User eventsInitiator) {
        return new Subscription(dto.getId(), subscriber, eventsInitiator);
    }

    public static SubscriptionDto mapToDto(Subscription subscription) {
        return new SubscriptionDto(subscription.getId(),
                                   UserMapper.mapToShortDto(subscription.getSubscriber()),
                                   UserMapper.mapToShortDto(subscription.getEventsInitiator()));
    }
}
