package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService; // FilmService зависит от UserService, лайк только у существующего user

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        findById(film.getId());
        return filmStorage.update(film);
    }

    public Film findById(long id) {
        return filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + id + " не найден."));
    }

    public void addLike(long filmId, long userId) {
        Film film = findById(filmId);
        userService.findById(userId);
        film.getLikes().add(userId);
    }

    public void removeLike(long filmId, long userId) {
        Film film = findById(filmId);
        userService.findById(userId);
        film.getLikes().remove(userId);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size())) // Сортировка по убыванию лайков
                .limit(count)
                .collect(Collectors.toList());
    }
}