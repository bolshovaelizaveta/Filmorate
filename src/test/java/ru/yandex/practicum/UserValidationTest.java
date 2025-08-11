package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.User;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void userValidationSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("valid_login");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Должно быть 0 нарушений валидации для корректного пользователя");
    }

    @Test
    void userEmailBlankValidationFails() {
        User user = new User();
        user.setEmail("");
        user.setLogin("valid_login");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должно быть 1 нарушение для пустого email");
        assertEquals("Электронная почта не может быть пустой.", violations.iterator().next().getMessage(),
                "Сообщение об ошибке для пустого email неверно");
    }

    @Test
    void userEmailInvalidFormatValidationFails() {
        User user = new User();
        user.setEmail("invalid.email.com");
        user.setLogin("valid_login");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должно быть 1 нарушение для email без '@'");
        assertEquals("Некорректный формат электронной почты.", violations.iterator().next().getMessage(),
                "Сообщение об ошибке для неверного формата email неверно");
    }

    @Test
    void userLoginBlankValidationFails() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(2, violations.size(), "Должно быть 2 нарушения для пустого логина");

        Set<String> violationMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertTrue(violationMessages.contains("Логин не может быть пустым."),
                "Должно содержаться сообщение о пустом логине.");
        assertTrue(violationMessages.contains("Логин не может содержать пробелы."),
                "Должно содержаться сообщение о пробелах в логине.");
    }

    @Test
    void userLoginWithSpacesValidationFails() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("invalid login");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должно быть 1 нарушение для логина с пробелами");
        assertEquals("Логин не может содержать пробелы.", violations.iterator().next().getMessage(),
                "Сообщение об ошибке для логина с пробелами неверно");
    }

    @Test
    void userBirthdayInFutureValidationFails() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("valid_login");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должно быть 1 нарушение для даты рождения в будущем");
        assertEquals("Дата рождения не может быть в будущем.", violations.iterator().next().getMessage(),
                "Сообщение об ошибке для даты рождения в будущем неверно");
    }

    @Test
    void userNameBlankValidationSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("valid_login");
        user.setName("");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Не должно быть нарушений для пустого имени");
    }
}
