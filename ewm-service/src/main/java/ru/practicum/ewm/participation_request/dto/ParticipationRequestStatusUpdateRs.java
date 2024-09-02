package ru.practicum.ewm.participation_request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ParticipationRequestStatusUpdateRs {
    private final List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
    private final List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
}