package cat.liceu.drawings.repo;

import cat.liceu.drawings.models.Drawing;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DrawingRepoImpl implements DrawingRepo {

    private final JdbcTemplate jdbcTemplate;

    public DrawingRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Drawing drawing) {
        Drawing existent = findById(drawing.getId());

        if (existent != null) {
            jdbcTemplate.update(
                    "UPDATE drawings SET name = ?, owner_username = ?, canvas_width = ?, canvas_height = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?",
                    drawing.getName(), drawing.getOwnerUsername(), drawing.getWidth(), drawing.getHeight(), drawing.getId());
        } else {
            jdbcTemplate.update(
                    "INSERT INTO drawings (id, name, owner_username, canvas_width, canvas_height) VALUES (?, ?, ?, ?, ?)",
                    drawing.getId(), drawing.getName(), drawing.getOwnerUsername(), drawing.getWidth(), drawing.getHeight());
        }
    }

    @Override
    public Drawing findById(String id) {
        if (id == null) return null;
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM drawings WHERE id = ?",
                    (rs, rowNum) -> mapDrawing(rs),
                    id
            );
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public List<Drawing> findByUsername(String username) {
        if (username == null) return List.of();
        return jdbcTemplate.query(
                "SELECT * FROM drawings WHERE owner_username = ? ORDER BY updated_at DESC",
                (rs, rowNum) -> mapDrawing(rs),
                username
        );
    }

    @Override
    public List<Drawing> findAllExceptUsername(String username) {
        if (username == null) return List.of();
        return jdbcTemplate.query(
                "SELECT * FROM drawings WHERE owner_username <> ? ORDER BY updated_at DESC",
                (rs, rowNum) -> mapDrawing(rs),
                username
        );
    }

    @Override
    public void deleteByIdAndUsername(String id, String username) {
        if (id == null || username == null) return;
        jdbcTemplate.update("DELETE FROM drawings WHERE id = ? AND owner_username = ?", id, username);
    }

    private Drawing mapDrawing(ResultSet rs) throws SQLException {
        Drawing d = new Drawing(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("owner_username"),
                new ArrayList<>(),
                rs.getInt("canvas_width"),
                rs.getInt("canvas_height")
        );
        d.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        d.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return d;
    }
}