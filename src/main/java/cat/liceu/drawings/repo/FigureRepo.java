package cat.liceu.drawings.repo;

import cat.liceu.drawings.models.GeometricFigure;

import java.util.List;

public interface FigureRepo {
    List<GeometricFigure> findByDrawingId(String drawingId);
}
