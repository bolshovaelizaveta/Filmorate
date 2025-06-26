package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j // Аннотация для автоматического создания логгера
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    private long nextId = 1;

    private long getNextId() {
        return nextId++;
    }

    private void validateFilm(Film film) {
        // Дата релиза — не раньше 28 декабря 1895 года LocalDate.of(1895, 12, 28)
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Валидация фильма не пройдена: Дата релиза раньше 28.12.1895. Фильм: {}", film);
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года.");
        }

        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Валидация фильма не пройдена: Название фильма пустое. Фильм: {}", film);
            throw new ValidationException("Название фильма не может быть пустым.");
        }
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос GET /films. Количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST /films. Попытка добавить фильм: {}", film);
        validateFilm(film);

        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм успешно добавлен: {}", film);
        return film;
    }

    // ВАЖНО: Добавлена пустая строка между методами для соответствия стандартам форматирования
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT /films. Попытка обновить фильм: {}", film);
        if (film.getId() == 0 || !films.containsKey(film.getId())) {
            log.warn("Валидация обновления фильма не пройдена: Фильм с ID {} не найден.", film.getId());
            throw new ValidationException("Фильм с таким ID не найден.");
        }

        validateFilm(film);

        films.put(film.getId(), film);
        log.info("Фильм успешно обновлен: {}", film);
        return film;
    }
}
