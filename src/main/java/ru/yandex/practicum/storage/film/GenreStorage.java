package ru.yandex.practicum.storage.film;

import ru.yandex.practicum.model.Genre;
import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {
    Collection<Genre> findAll();

    Optional<Genre> findById(int id);
}