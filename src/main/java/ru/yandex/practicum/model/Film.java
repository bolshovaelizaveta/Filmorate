package ru.yandex.practicum.model;

import lombok.Data;

import java.time.LocalDate;
import ru.yandex.practicum.validation.PastCinemaHistory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Data
public class Film {
    private long id;

    @NotBlank(message = "Название не может быть пустым.")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания - 200 символов.")
    private String description;

    @PastCinemaHistory
    @PastOrPresent(message = "Дата релиза не может быть в будущем.")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом.")
    private int duration;
}
