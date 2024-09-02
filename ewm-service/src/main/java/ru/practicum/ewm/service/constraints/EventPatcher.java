package ru.practicum.ewm.service.constraints;

import jakarta.validation.groups.Default;

public interface EventPatcher {
    interface Admin extends Default {}

    interface Initiator extends Default {}
}
