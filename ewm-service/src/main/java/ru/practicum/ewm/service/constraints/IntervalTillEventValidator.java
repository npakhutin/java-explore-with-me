package ru.practicum.ewm.service.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Slf4j
public class IntervalTillEventValidator implements ConstraintValidator<IntervalTillEvent, LocalDateTime> {
    private Duration minIntervalTillEvent;

    @Override
    public void initialize(IntervalTillEvent constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        minIntervalTillEvent = Duration.parse(constraintAnnotation.strInterval());
    }

    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime minEventDate = LocalDateTime.now().plus(minIntervalTillEvent);
        if (eventDate == null || eventDate.isAfter(minEventDate)) {
            return true;
        } else {
            log.warn("Дата события должна быть позже {}",
                     minEventDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
            return false;
        }
    }
}
