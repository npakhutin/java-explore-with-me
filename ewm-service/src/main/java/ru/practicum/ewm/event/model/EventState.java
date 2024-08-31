package ru.practicum.ewm.event.model;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public enum EventState {
    PENDING, PUBLISHED, CANCELED
}
