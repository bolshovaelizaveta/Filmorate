package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.storage.film.FilmDbStorage;
import ru.yandex.practicum.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class})
class FilmDbStorageTest {

    private final FilmStorage filmStorage;

    @BeforeEach
    public void setup() {
        JdbcTemplate jdbcTemplate = ((FilmDbStorage) filmStorage).getJdbcTemplate();
        jdbcTemplate.update("DELETE FROM film_genres");
        jdbcTemplate.update("DELETE FROM likes");
        jdbcTemplate.update("DELETE FROM films");
    }

    @Test
    public void testCreateAndFindFilmById() {
        Film newFilm = createTestFilm("Film 1", "Description 1");
        Film createdFilm = filmStorage.create(newFilm);

        Optional<Film> filmOptional = filmStorage.findById(createdFilm.getId());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("id", createdFilm.getId())
                                .hasFieldOrPropertyWithValue("name", "Film 1")
                                .hasFieldOrPropertyWithValue("description", "Description 1")
                );
    }

    @Test
    public void testUpdateFilm() {
        Film filmToUpdate = filmStorage.create(createTestFilm("Before Update", "Desc Before"));

        filmToUpdate.setName("After Update");
        filmToUpdate.setDescription("Desc After");
        filmStorage.update(filmToUpdate);

        Optional<Film> updatedFilmOptional = filmStorage.findById(filmToUpdate.getId());
        assertThat(updatedFilmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "After Update")
                                .hasFieldOrPropertyWithValue("description", "Desc After")
                );
    }

    @Test
    public void testFindAllFilms() {
        filmStorage.create(createTestFilm("Film A", "Desc A"));
        filmStorage.create(createTestFilm("Film B", "Desc B"));

        Collection<Film> films = filmStorage.findAll();

        assertThat(films).hasSize(2);
        assertThat(films).extracting(Film::getName).containsExactlyInAnyOrder("Film A", "Film B");
    }

    private Film createTestFilm(String name, String description) {
        Film film = new Film();
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G"));
        film.setGenres(new HashSet<>());
        return film;
    }
}