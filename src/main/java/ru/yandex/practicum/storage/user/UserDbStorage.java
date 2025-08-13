package ru.yandex.practicum.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAll() {
        String sql = "SELECT * FROM users";
        Collection<User> users = jdbcTemplate.query(sql, this::mapRowToUser);
        loadFriendsForUsers(users);
        return users;
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"user_id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        long userId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        user.setId(userId);
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        return user;
    }

    @Override
    public Optional<User> findById(long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        Optional<User> userOpt = jdbcTemplate.query(sql, this::mapRowToUser, id).stream().findFirst();
        userOpt.ifPresent(this::loadFriends);
        return userOpt;
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        return user;
    }

    public void addFriend(long userId, long friendId) {
        String sql = "INSERT INTO user_friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        String sql = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    private void loadFriends(User user) {
        String sql = "SELECT friend_id FROM user_friends WHERE user_id = ?";
        List<Long> friendIds = jdbcTemplate.queryForList(sql, Long.class, user.getId());
        user.getFriends().addAll(friendIds);
    }

    private void loadFriendsForUsers(Collection<User> users) {
        if (users.isEmpty()) {
            return;
        }
        String userIds = users.stream()
                .map(u -> String.valueOf(u.getId()))
                .collect(Collectors.joining(","));
        String sql = "SELECT user_id, friend_id FROM user_friends WHERE user_id IN (" + userIds + ")";
        Map<Long, Set<Long>> friendsByUserId = new HashMap<>();
        jdbcTemplate.query(sql, rs -> {
            long userId = rs.getLong("user_id");
            long friendId = rs.getLong("friend_id");
            friendsByUserId.computeIfAbsent(userId, k -> new HashSet<>()).add(friendId);
        });
        users.forEach(u -> u.getFriends().addAll(friendsByUserId.getOrDefault(u.getId(), Collections.emptySet())));
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}