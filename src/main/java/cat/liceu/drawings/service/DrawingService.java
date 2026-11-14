package cat.liceu.drawings.service;

import cat.liceu.drawings.models.Drawing;
import cat.liceu.drawings.models.GeometricFigure;
import cat.liceu.drawings.repo.DrawingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DrawingService {

    @Autowired
    private DrawingRepo drawingRepo;

    public Drawing saveDrawing(String id, String name, String username, List<GeometricFigure> figures,
                               int width, int height) {

        if (name == null || name.isBlank()) {
            int randomNumber = (int) (Math.random() * 1000);
            name = "Dibuix: " + randomNumber;
        }

        if (id == null || id.isEmpty()){
            return createDrawing(name, username, figures, width, height);
        } else {
            return updateDrawing(id, name, username, figures, width, height);
        }
    }

    private Drawing updateDrawing(String id, String name, String username, List<GeometricFigure> figures,
                                  int width, int height) {
        Drawing existingDrawing = drawingRepo.findById(id);

        if (existingDrawing != null && username.equals(existingDrawing.getOwnerUsername())) {
            existingDrawing.setName(name);
            existingDrawing.setFigures(figures);
            existingDrawing.setWidth(width);
            existingDrawing.setHeight(height);
            existingDrawing.setUpdatedAt(LocalDateTime.now());
            drawingRepo.save(existingDrawing);
        }
        return existingDrawing;
    }

    private Drawing createDrawing(String name, String username, List<GeometricFigure> figures,
                                  int width, int height) {
        String id = java.util.UUID.randomUUID().toString();

        Drawing drawing = new Drawing(id, name, username, figures, width, height);

        drawingRepo.save(drawing);

        return drawing;
    }

    public List<Drawing> getMyDrawings(String username) {
        return drawingRepo.findByUsername(username);
    }

    public List<Drawing> getOthersDrawings(String username) {
        return drawingRepo.findAllExceptUsername(username);
    }

    public Drawing getDrawingById(String id) {
        return drawingRepo.findById(id);
    }

    public boolean deleteDrawing(String id, String username) {
        Drawing drawing = drawingRepo.findById(id);
        if (drawing == null || !username.equals(drawing.getOwnerUsername())) {
            return false;
        }

        drawingRepo.deleteByIdAndUsername(id, username);
        return true;
    }
}