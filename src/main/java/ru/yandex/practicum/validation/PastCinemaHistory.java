package ru.yandex.practicum.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented // Указывает, что этот Javadoc должен быть включен в сгенерированный Javadoc
@Constraint(validatedBy = PastCinemaHistoryValidator.class) // Связывает аннотацию с её валидатором
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PastCinemaHistory {
    String message() default "Дата релиза фильма не может быть раньше 28 декабря 1895 года.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
