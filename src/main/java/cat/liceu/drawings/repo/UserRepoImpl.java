package cat.liceu.drawings.repo;

import cat.liceu.drawings.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepoImpl implements UserRepo {

    private final JdbcTemplate jdbcTemplate;

    public UserRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findByUsername(String username) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT name, username, password FROM users WHERE username = ?",
                    (resultSet, rowNumber) -> new User(
                            resultSet.getString("name"),
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    ),
                    username
            );
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update(
                "INSERT INTO users (name, username, password) VALUES (?, ?, ?)",
                user.getName(), user.getUsername(), user.getPassword()
        );
    }
}
