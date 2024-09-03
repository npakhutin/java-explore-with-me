package ru.practicum.ewm.subscription;

import ru.practicum.ewm.user.dto.UserShortDto;

import java.util.List;

public interface SubscriptionService {
    UserShortDto addNewSubscription(Long subscriberId, Long eventsInitiatorId);

    void deleteSubscription(Long subscriberId, Long eventsInitiatorId);

    List<UserShortDto> findAllSubscriptions(Long subscriberId, Integer start, Integer size);

    void deleteSubscriptionsToEventsInitiator(Long eventsInitiatorId);

    void deleteUserSubscriptions(Long subscriberId);
}
