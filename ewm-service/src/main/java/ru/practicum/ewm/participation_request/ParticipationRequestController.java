package ru.practicum.ewm.participation_request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.participation_request.dto.ParticipationRequestDto;
import ru.practicum.ewm.participation_request.dto.ParticipationRequestStatusUpdateRq;
import ru.practicum.ewm.participation_request.dto.ParticipationRequestStatusUpdateRs;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class ParticipationRequestController {
    private final ParticipationRequestService requestService;

    @GetMapping("/users/{requesterId}/requests")
    public List<ParticipationRequestDto> findUserParticipationRequests(@PathVariable Long requesterId) {
        return requestService.findUserParticipationRequests(requesterId);
    }

    @PostMapping("/users/{requesterId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addNewParticipationRequest(@PathVariable Long requesterId,
                                                              @RequestParam(name = "eventId") Long eventId) {
        return requestService.addNewParticipationRequest(requesterId, eventId);
    }

    @PatchMapping("/users/{requesterId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelUserParticipationRequest(@PathVariable Long requesterId,
                                                                  @PathVariable Long requestId) {
        return requestService.cancelUserParticipationRequest(requesterId, requestId);
    }

    @PatchMapping("/users/{initiatorId}/events/{eventId}/requests")
    public ParticipationRequestStatusUpdateRs updateEventParticipationRequestsStatus(@PathVariable Long initiatorId,
                                                                                     @PathVariable Long eventId,
                                                                                     @RequestBody ParticipationRequestStatusUpdateRq statusUpdateRq) {
        return requestService.updateEventParticipationRequestsStatus(initiatorId, eventId, statusUpdateRq);
    }

    @GetMapping("/users/{initiatorId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> findEventParticipationRequests(@PathVariable Long initiatorId,
                                                                        @PathVariable Long eventId) {
        return requestService.findEventParticipationRequests(initiatorId, eventId);
    }
}
