package ru.practicum.ewm.participation_request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.participation_request.model.ParticipationRequestStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipationRequestStatusUpdateRq {
    private List<Long> requestIds;
    private ParticipationRequestStatus status;
}