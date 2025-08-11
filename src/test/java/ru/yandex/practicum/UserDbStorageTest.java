package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.UserDbStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
class UserDbStorageTest {

    private final UserStorage userStorage;

    @BeforeEach
    public void setup() {
        JdbcTemplate jdbcTemplate = ((UserDbStorage) userStorage).getJdbcTemplate();
        jdbcTemplate.update("DELETE FROM user_friends");
        jdbcTemplate.update("DELETE FROM users");
    }

    @Test
    public void testCreateAndFindUserById() {
        User newUser = new User();
        newUser.setEmail("test@test.com");
        newUser.setLogin("testlogin");
        newUser.setName("Test Name");
        newUser.setBirthday(LocalDate.of(2000, 1, 1));
        User createdUser = userStorage.create(newUser);

        Optional<User> userOptional = userStorage.findById(createdUser.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", createdUser.getId())
                                .hasFieldOrPropertyWithValue("email", "test@test.com")
                );
    }

    @Test
    public void testUpdateUser() {
        User newUser = new User();
        newUser.setEmail("update@test.com");
        newUser.setLogin("update_login");
        newUser.setName("Before Update");
        newUser.setBirthday(LocalDate.of(1999, 1, 1));
        User createdUser = userStorage.create(newUser);

        createdUser.setName("After Update");
        userStorage.update(createdUser);

        Optional<User> updatedUserOptional = userStorage.findById(createdUser.getId());
        assertThat(updatedUserOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "After Update")
                );
    }

    @Test
    public void testFindAllUsers() {
        User user1 = new User();
        user1.setEmail("user1@test.com");
        user1.setLogin("user1");
        user1.setBirthday(LocalDate.now());
        userStorage.create(user1);

        User user2 = new User();
        user2.setEmail("user2@test.com");
        user2.setLogin("user2");
        user2.setBirthday(LocalDate.now());
        userStorage.create(user2);

        Collection<User> users = userStorage.findAll();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getLogin).containsExactlyInAnyOrder("user1", "user2");
    }
}
