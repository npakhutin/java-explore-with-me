package ru.practicum.ewm.subscription;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.user.User;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Page<Subscription> findAllBySubscriber(User subscriber, PageRequest pageable);

    void deleteByEventsInitiator(User eventsInitiator);

    void deleteBySubscriber(User subscriber);
}
