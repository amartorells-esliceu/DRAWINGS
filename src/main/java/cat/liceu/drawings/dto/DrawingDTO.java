package cat.liceu.drawings.dto;

import cat.liceu.drawings.models.GeometricFigure;
import java.util.List;

public class DrawingDTO {
    private String id;
    private String name;
    private List<GeometricFigure> figures;
    private int width = 800;
    private int height = 500;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<GeometricFigure> getFigures() { return figures; }
    public void setFigures(List<GeometricFigure> figures) { this.figures = figures; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
}