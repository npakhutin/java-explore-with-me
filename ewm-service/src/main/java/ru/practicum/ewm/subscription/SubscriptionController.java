package ru.practicum.ewm.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping("/users/{subscriberId}/subscriptions")
    public List<UserShortDto> findAllSubscriptions(@PathVariable Long subscriberId,
                                                   @RequestParam(name = "from", defaultValue = "0") Integer start,
                                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return subscriptionService.findAllSubscriptions(subscriberId, start, size);
    }

    @DeleteMapping("/users/{subscriberId}/subscriptions/{eventsInitiatorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long subscriberId, @PathVariable Long eventsInitiatorId) {
        subscriptionService.deleteSubscription(subscriberId, eventsInitiatorId);
    }

    @PostMapping("/users/{subscriberId}/subscriptions/{eventsInitiatorId}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserShortDto addNewSubscription(@PathVariable Long subscriberId, @PathVariable Long eventsInitiatorId) {
        return subscriptionService.addNewSubscription(subscriberId, eventsInitiatorId);
    }

    @DeleteMapping("/admin/subscriptions/{eventsInitiatorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscriptionsToEventsInitiator(@PathVariable Long eventsInitiatorId) {
        subscriptionService.deleteSubscriptionsToEventsInitiator(eventsInitiatorId);
    }

    @DeleteMapping("/admin/users/subscriptions/{subscriberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserSubscriptions(@PathVariable Long subscriberId) {
        subscriptionService.deleteUserSubscriptions(subscriberId);
    }
}
