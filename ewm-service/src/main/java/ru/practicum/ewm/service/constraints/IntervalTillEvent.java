package ru.practicum.ewm.service.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Repeatable(IntervalTillEventContainer.class)
@Retention(RUNTIME)
@Constraint(validatedBy = IntervalTillEventValidator.class)
@Documented
public @interface IntervalTillEvent {
    String message() default "{IntervalTillEvent.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String strInterval();
}
