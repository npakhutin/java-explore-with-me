package ru.practicum.ewm.participation_request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.participation_request.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findByRequesterId(Long requesterId);

    List<ParticipationRequest> findByEventId(Long eventId);

    ParticipationRequest findByIdAndRequesterId(Long requestId, Long requesterId);
}
