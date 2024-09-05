package ru.practicum.ewm.subscription;

import java.util.List;

public interface SubscriptionService {
    SubscriptionDto addNewSubscription(Long subscriberId, Long eventsInitiatorId);

    void deleteSubscription(Long subscriptionId);

    List<SubscriptionDto> findAllUserSubscriptions(Long subscriberId, Integer start, Integer size);

    void deleteSubscriptionsToEventsInitiator(Long eventsInitiatorId);

    void deleteUserSubscriptions(Long subscriberId);
}
