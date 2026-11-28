package cat.liceu.drawings.repo;

import cat.liceu.drawings.models.GeometricFigure;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FigureRepoImpl implements FigureRepo {

    private final JdbcTemplate jdbcTemplate;

    public FigureRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GeometricFigure> findByDrawingId(String drawingId) {
        if (drawingId == null) {
            return List.of();
        }

        return jdbcTemplate.query(
                "SELECT id, name, figure_type, color, x, y, size " +
                        "FROM figures WHERE drawing_id = ? ORDER BY order_index",
                (rs, rowNumber) -> mapFigure(rs), drawingId
        );
    }

    private List<GeometricFigure.Point> findPointsByFigureId(long figureId) {
        return jdbcTemplate.query(
                "SELECT x, y FROM figure_points WHERE figure_id = ? ORDER BY order_index",
                (rs, rowNumber) -> new GeometricFigure.Point(rs.getInt("x"), rs.getInt("y")),
                figureId
        );
    }

    private GeometricFigure mapFigure(ResultSet rs) throws SQLException {
        GeometricFigure figure = new GeometricFigure();
        figure.setName(rs.getString("name"));
        figure.setType(rs.getString("figure_type"));
        figure.setColor(rs.getString("color"));
        figure.setX(rs.getInt("x"));
        figure.setY(rs.getInt("y"));
        figure.setSize(rs.getInt("size"));
        figure.setPoints(findPointsByFigureId(rs.getLong("id")));
        return figure;
    }
}