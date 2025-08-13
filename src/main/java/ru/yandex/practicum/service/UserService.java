package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.UserDbStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        findById(user.getId());
        return userStorage.update(user);
    }

    public User findById(long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + id + " не найден."));
    }

    public void addFriend(long userId, long friendId) {
        findById(userId);
        findById(friendId);
        ((UserDbStorage) userStorage).addFriend(userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        findById(userId);
        findById(friendId);
        ((UserDbStorage) userStorage).removeFriend(userId, friendId);
    }

    public List<User> getFriends(long userId) {
        User user = findById(userId);
        return user.getFriends().stream()
                .map(this::findById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        User user = findById(userId);
        User other = findById(otherId);

        Set<Long> userFriends = user.getFriends();
        Set<Long> otherFriends = other.getFriends();

        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(this::findById)
                .collect(Collectors.toList());
    }
}