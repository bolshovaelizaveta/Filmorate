package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.storage.film.MpaDbStorage;
import ru.yandex.practicum.storage.film.MpaStorage;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaDbStorage.class})
class MpaDbStorageTest {

    private final MpaStorage mpaStorage;

    @Test
    public void testFindAllMpaRatings() {
        Collection<Mpa> mpaRatings = mpaStorage.findAll();

        assertThat(mpaRatings).hasSize(5);

        assertThat(mpaRatings)
                .extracting(Mpa::getName)
                .containsExactly("G", "PG", "PG-13", "R", "NC-17");
    }

    @Test
    public void testFindMpaById() {
        Optional<Mpa> mpaOptional = mpaStorage.findById(1);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa)
                                .hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("name", "G")
                );
    }

    @Test
    public void testFindMpaByNonExistentId() {
        Optional<Mpa> mpaOptional = mpaStorage.findById(999);

        assertThat(mpaOptional).isNotPresent();
    }
}