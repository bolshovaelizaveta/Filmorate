package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    private long getNextId() {
        return nextId++;
    }

    private void validateUser(User user) {

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Валидация пользователя не пройдена: Логин пустой или содержит пробелы. Пользователь: {}", user);
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Валидация пользователя не пройдена: Дата рождения в будущем. Пользователь: {}", user);
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Валидация пользователя не пройдена: Email пустой или не содержит '@'. Пользователь: {}", user);
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ '@'.");
        }
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос GET /users. Количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос POST /users. Попытка создать пользователя: {}", user);
        validateUser(user);

        boolean isEmailDuplicated = users.values().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()));
        if (isEmailDuplicated) {
            log.warn("Валидация создания пользователя не пройдена: Этот email уже используется. Пользователь: {}", user);
            throw new ValidationException("Этот имейл уже используется.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(getNextId());

        users.put(user.getId(), user);
        log.info("Пользователь успешно создан: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT /users. Попытка обновить пользователя: {}", user);
        if (user.getId() == 0 || !users.containsKey(user.getId())) {
            log.warn("Валидация обновления пользователя не пройдена: Пользователь с ID {} не найден.", user.getId());
            throw new ValidationException("Пользователь с таким ID не найден.");
        }

        validateUser(user);

        User existingUser = users.get(user.getId());

        if (user.getEmail() != null && !user.getEmail().equalsIgnoreCase(existingUser.getEmail())) {
            boolean isEmailDuplicated = users.values().stream()
                    .anyMatch(u -> u.getId() != user.getId() && u.getEmail().equalsIgnoreCase(user.getEmail()));
            if (isEmailDuplicated) {
                log.warn("Валидация обновления пользователя не пройдена: Новый email уже используется другим пользователем. Пользователь: {}", user);
                throw new ValidationException("Этот имейл уже используется.");
            }
        }

        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getLogin() != null) {
            existingUser.setLogin(user.getLogin());
        }
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        } else if (user.getName() != null && user.getName().isBlank()) {
            existingUser.setName(existingUser.getLogin());
        }
        if (user.getBirthday() != null) {
            existingUser.setBirthday(user.getBirthday());
        }

        if (user.getName() != null && user.getName().isBlank()) {
            existingUser.setName(existingUser.getLogin());
        } else if (user.getName() != null) {
            existingUser.setName(user.getName());
        }

        users.put(existingUser.getId(), existingUser);
        log.info("Пользователь успешно обновлен: {}", existingUser);
        return existingUser;
    }
}