package ru.yandex.practicum.storage.film;

import ru.yandex.practicum.model.Film;
import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Optional<Film> findById(long id);
}