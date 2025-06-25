package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ru.yandex.practicum.controller.FilmController;
import ru.yandex.practicum.controller.UserController;

@SpringBootTest
class FilmorateApplicationTests {

    private final FilmController filmController;
    private final UserController userController;

    @Autowired
    public FilmorateApplicationTests(FilmController filmController, UserController userController) {
        this.filmController = filmController;
        this.userController = userController;
    }

    @Test
    void contextLoads() {
        // Контроллеры не должны быть null после загрузки
        assertNotNull(filmController, "FilmController должен быть создан Spring контекстом.");
        assertNotNull(userController, "UserController должен быть создан Spring контекстом.");
    }

}