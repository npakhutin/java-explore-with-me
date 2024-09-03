package ru.practicum.ewm.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.exception.BadRequestException;
import ru.practicum.ewm.service.exception.NotFoundException;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserMapper;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    private final UserRepository userRepository;

    @Override
    public UserShortDto addNewSubscription(Long subscriberId, Long eventsInitiatorId) {
        if ((Objects.equals(subscriberId, eventsInitiatorId))) {
            throw new BadRequestException("Пользователь не может подписаться на самого себя");
        }
        User subscriber = userRepository.findById(subscriberId)
                                        .orElseThrow(() -> new NotFoundException(
                                                "Не найден пользователь id = " + subscriberId));
        User eventsInitiator = userRepository.findById(eventsInitiatorId)
                                             .orElseThrow(() -> new NotFoundException(
                                                     "Не найден пользователь id = " + eventsInitiatorId));
        if (subscriber.getSubscriptions().contains(eventsInitiator)) {
            throw new BadRequestException("Пользователь уже подписан события указанного инициатора");
        }
        subscriber.getSubscriptions().add(eventsInitiator);
        userRepository.save(subscriber);

        return UserMapper.mapToShortDto(eventsInitiator);
    }

    @Override
    public void deleteSubscription(Long subscriberId, Long eventsInitiatorId) {
        User subscriber = userRepository.findById(subscriberId)
                                        .orElseThrow(() -> new NotFoundException(
                                                "Не найден пользователь id = " + subscriberId));
        User eventsInitiator = userRepository.findById(eventsInitiatorId)
                                             .orElseThrow(() -> new NotFoundException(
                                                     "Не найден пользователь id = " + eventsInitiatorId));
        subscriber.getSubscriptions().remove(eventsInitiator);
        userRepository.save(subscriber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserShortDto> findAllSubscriptions(Long subscriberId, Integer start, Integer size) {
        User subscriber = userRepository.findById(subscriberId)
                                        .orElseThrow(() -> new NotFoundException(
                                                "Не найден пользователь id = " + subscriberId));
        if (subscriber.getSubscriptions().isEmpty() || start >= subscriber.getSubscriptions().size()) {
            return new ArrayList<>();
        }
        int end = Math.min(size, subscriber.getSubscriptions().size());
        return subscriber.getSubscriptions()
                         .subList(start, end)
                         .stream()
                         .map(UserMapper::mapToShortDto)
                         .collect(Collectors.toList());
    }

    @Override
    public void deleteSubscriptionsToEventsInitiator(Long eventsInitiatorId) {
        User eventsInitiator = userRepository.findById(eventsInitiatorId)
                                             .orElseThrow(() -> new NotFoundException(
                                                     "Не найден пользователь id = " + eventsInitiatorId));
        List<User> subscribers = userRepository.findAllSubscribers(eventsInitiator);
        for (User subscriber : subscribers) {
            subscriber.getSubscriptions().remove(eventsInitiator);
        }
        userRepository.saveAll(subscribers);
    }

    @Override
    public void deleteUserSubscriptions(Long subscriberId) {
        User subscriber = userRepository.findById(subscriberId)
                                        .orElseThrow(() -> new NotFoundException(
                                                "Не найден пользователь id = " + subscriberId));
        subscriber.getSubscriptions().clear();
        userRepository.save(subscriber);
    }
}
