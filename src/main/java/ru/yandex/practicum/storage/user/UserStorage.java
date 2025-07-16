package ru.yandex.practicum.storage.user;

import ru.yandex.practicum.model.User;
import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAll();

    User create(User user);

    User update(User user);

    Optional<User> findById(long id);
}