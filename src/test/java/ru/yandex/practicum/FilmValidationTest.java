package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.Film;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void filmValidationSuccess() {
        Film film = new Film();
        film.setName("Valid Film Name");
        film.setDescription("A valid description for a film, not too long.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(90);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Должно быть 0 нарушений валидации для корректного фильма");
    }

    @Test
    void filmNameBlankValidationFails() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(90);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должно быть 1 нарушение для пустого названия");
        assertEquals("Название не может быть пустым.", violations.iterator().next().getMessage(),
                "Сообщение об ошибке для пустого названия неверно");
    }

    @Test
    void filmDescriptionTooLongValidationFails() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("a".repeat(201));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(90);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должно быть 1 нарушение для описания длиннее 200 символов");
        assertEquals("Максимальная длина описания - 200 символов.", violations.iterator().next().getMessage(),
                "Сообщение об ошибке для слишком длинного описания неверно");
    }

    @Test
    void filmReleaseDateTooEarlyValidationFails() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Description.");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(90);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должно быть 1 нарушение для даты релиза раньше 1895-12-28");
        assertEquals("Дата релиза фильма не может быть раньше 28 декабря 1895 года.", violations.iterator().next().getMessage(),
                "Сообщение об ошибке для слишком ранней даты релиза неверно");
    }

    @Test
    void filmDurationNonPositiveValidationFails() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(0);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должно быть 1 нарушение для неположительной продолжительности");
        assertEquals("Продолжительность фильма должна быть положительным числом.", violations.iterator().next().getMessage(),
                "Сообщение об ошибке для неположительной продолжительности неверно");
    }
}