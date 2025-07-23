package ru.yandex.practicum.storage.film;

import ru.yandex.practicum.model.Mpa;
import java.util.Collection;
import java.util.Optional;

public interface MpaStorage {
    Collection<Mpa> findAll();

    Optional<Mpa> findById(int id);
}