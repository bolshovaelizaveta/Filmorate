package ru.yandex.practicum.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class PastCinemaHistoryValidator implements ConstraintValidator<PastCinemaHistory, LocalDate> {

    // Дата 28 декабря 1895 года
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(PastCinemaHistory constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true;
        }
        // Проверяем, что дата не раньше дня рождения кинематографа
        return !date.isBefore(CINEMA_BIRTHDAY);
    }
}
