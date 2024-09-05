package ru.practicum.ewm.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.exception.BadRequestException;
import ru.practicum.ewm.service.exception.NotFoundException;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public SubscriptionDto addNewSubscription(Long subscriberId, Long eventsInitiatorId) {
        if ((Objects.equals(subscriberId, eventsInitiatorId))) {
            throw new BadRequestException("Пользователь не может подписаться на самого себя");
        }
        User subscriber = userRepository.findById(subscriberId)
                                        .orElseThrow(() -> new NotFoundException(
                                                "Не найден пользователь id = " + subscriberId));
        User eventsInitiator = userRepository.findById(eventsInitiatorId)
                                             .orElseThrow(() -> new NotFoundException(
                                                     "Не найден пользователь id = " + eventsInitiatorId));
        Subscription subscription = new Subscription(null, subscriber, eventsInitiator);
        return SubscriptionMapper.mapToDto(subscriptionRepository.save(subscription));
    }

    @Override
    public void deleteSubscription(Long subscriptionId) {
        subscriptionRepository.deleteById(subscriptionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionDto> findAllUserSubscriptions(Long subscriberId, Integer start, Integer size) {
        User subscriber = userRepository.findById(subscriberId)
                                        .orElseThrow(() -> new NotFoundException(
                                                "Не найден пользователь id = " + subscriberId));
        int pageNumber = size != 0 ? start / size : 0;
        PageRequest pageable = PageRequest.of(pageNumber, size, Sort.by(User.Fields.id).ascending());

        return subscriptionRepository.findAllBySubscriber(subscriber, pageable)
                                     .stream()
                                     .map(SubscriptionMapper::mapToDto)
                                     .collect(Collectors.toList());
    }

    @Override
    public void deleteSubscriptionsToEventsInitiator(Long eventsInitiatorId) {
        User eventsInitiator = userRepository.findById(eventsInitiatorId)
                                             .orElseThrow(() -> new NotFoundException(
                                                     "Не найден пользователь id = " + eventsInitiatorId));
        subscriptionRepository.deleteByEventsInitiator(eventsInitiator);
    }

    @Override
    public void deleteUserSubscriptions(Long subscriberId) {
        User subscriber = userRepository.findById(subscriberId)
                                        .orElseThrow(() -> new NotFoundException(
                                                "Не найден пользователь id = " + subscriberId));
        subscriptionRepository.deleteBySubscriber(subscriber);
    }
}
