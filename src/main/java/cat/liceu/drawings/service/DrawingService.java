package cat.liceu.drawings.service;

import cat.liceu.drawings.models.Drawing;
import cat.liceu.drawings.models.GeometricFigure;
import cat.liceu.drawings.repo.DrawingRepo;
import cat.liceu.drawings.repo.FigureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DrawingService {

    @Autowired
    private DrawingRepo drawingRepo;

    @Autowired
    private FigureRepo figureRepo;

    public Drawing saveDrawing(String id, String name, String username, List<GeometricFigure> figures,
                               int width, int height) {

        if (username == null) {
            throw new IllegalArgumentException("Cal iniciar sessió per desar un dibuix");
        }

        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Les dimensions del canvas han de ser positives");
        }

        if (name == null || name.isBlank()) {
            int randomNumber = (int) (Math.random() * 1000);
            name = "Dibuix: " + randomNumber;
        }

        List<GeometricFigure> safeFigures = figures;
        if (safeFigures == null) {
            safeFigures = List.of();
        }

        if (id == null || id.isEmpty()) {
            return createDrawing(name, username, safeFigures, width, height);
        }

        return updateDrawing(id, name, username, safeFigures, width, height);
    }

    private Drawing createDrawing(String name, String username, List<GeometricFigure> figures,
                                  int width, int height) {
        String id = UUID.randomUUID().toString();
        Drawing drawing = new Drawing(id, name, username, figures, width, height);
        drawingRepo.save(drawing);
        return drawing;
    }

    private Drawing updateDrawing(String id, String name, String username, List<GeometricFigure> figures,
                                  int width, int height) {
        Drawing existing = drawingRepo.findById(id);
        if (existing == null || !username.equals(existing.getOwnerUsername())) {
            throw new IllegalArgumentException("No es pot editar aquest dibuix");
        }

        existing.setName(name);
        existing.setFigures(figures);
        existing.setWidth(width);
        existing.setHeight(height);
        existing.setUpdatedAt(LocalDateTime.now());

        drawingRepo.save(existing);
        return existing;
    }

    public List<Drawing> getMyDrawings(String username) {
        return loadFigures(drawingRepo.findByUsername(username));
    }

    public List<Drawing> getOthersDrawings(String username) {
        return loadFigures(drawingRepo.findAllExceptUsername(username));
    }

    public Drawing getDrawingById(String id) {
        return loadFigures(drawingRepo.findById(id));
    }

    public void deleteDrawing(String id, String username) {
        Drawing drawing = drawingRepo.findById(id);
        if (drawing != null && username.equals(drawing.getOwnerUsername())) {
            drawingRepo.deleteByIdAndUsername(id, username);
        }
    }

    private Drawing loadFigures(Drawing drawing) {
        if (drawing != null) {
            drawing.setFigures(figureRepo.findByDrawingId(drawing.getId()));
        }
        return drawing;
    }

    private List<Drawing> loadFigures(List<Drawing> drawings) {
        for (Drawing drawing : drawings) {
            loadFigures(drawing);
        }
        return drawings;
    }
}