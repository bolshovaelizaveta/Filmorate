package ru.yandex.practicum.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT * FROM films f JOIN mpa m ON f.mpa_id = m.mpa_id";
        Collection<Film> films = jdbcTemplate.query(sql, this::mapRowToFilm);
        loadGenresForFilms(films);
        return films;
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        long filmId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(filmId);

        saveGenres(film);

        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        deleteGenres(film.getId());
        saveGenres(film);

        return findById(film.getId()).orElse(null);
    }

    @Override
    public Optional<Film> findById(long id) {
        String sql = "SELECT * FROM films f JOIN mpa m ON f.mpa_id = m.mpa_id WHERE f.film_id = ?";

        Optional<Film> filmOpt = jdbcTemplate.query(sql, this::mapRowToFilm, id)
                .stream()
                .findFirst();

        filmOpt.ifPresent(film -> loadGenresForFilms(Collections.singleton(film)));

        return filmOpt;
    }

    private void saveGenres(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        film.getGenres().forEach(genre -> jdbcTemplate.update(sql, film.getId(), genre.getId()));
    }

    private void deleteGenres(long filmId) {
        String sql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    private void loadGenresForFilms(Collection<Film> films) {
        if (films.isEmpty()) {
            return;
        }
        String filmIds = films.stream()
                .map(film -> String.valueOf(film.getId()))
                .collect(Collectors.joining(","));

        String sql = "SELECT * FROM film_genres fg JOIN genres g ON fg.genre_id = g.genre_id WHERE fg.film_id IN (" + filmIds + ")";

        Map<Long, Set<Genre>> genresByFilmId = new HashMap<>();
        jdbcTemplate.query(sql, rs -> {
            long filmId = rs.getLong("film_id");
            Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("name"));
            genresByFilmId.computeIfAbsent(filmId, k -> new HashSet<>()).add(genre);
        });

        films.forEach(film -> film.setGenres(genresByFilmId.getOrDefault(film.getId(), new HashSet<>())));
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));

        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("mpa_id"));
        mpa.setName(rs.getString("mpa.name"));
        film.setMpa(mpa);

        return film;
    }
}