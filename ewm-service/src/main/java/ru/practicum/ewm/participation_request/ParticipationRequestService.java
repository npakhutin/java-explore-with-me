package ru.practicum.ewm.participation_request;

import ru.practicum.ewm.participation_request.dto.ParticipationRequestDto;
import ru.practicum.ewm.participation_request.dto.ParticipationRequestStatusUpdateRq;
import ru.practicum.ewm.participation_request.dto.ParticipationRequestStatusUpdateRs;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequestDto addNewParticipationRequest(Long requesterId, Long eventId);

    List<ParticipationRequestDto> findUserParticipationRequests(Long requesterId);

    List<ParticipationRequestDto> findEventParticipationRequests(Long initiatorId, Long eventId);

    ParticipationRequestDto cancelUserParticipationRequest(Long requesterId, Long requestId);

    ParticipationRequestStatusUpdateRs updateEventParticipationRequestsStatus(Long initiatorId,
                                                                              Long eventId,
                                                                              ParticipationRequestStatusUpdateRq statusUpdateRq);
}
