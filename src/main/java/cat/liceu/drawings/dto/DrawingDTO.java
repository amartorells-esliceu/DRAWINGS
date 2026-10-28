package cat.liceu.drawings.dto;

import cat.liceu.drawings.models.GeometricFigure;
import java.util.List;

public class DrawingDTO {
    private String id;
    private String nom;
    private List<GeometricFigure> figures;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public List<GeometricFigure> getFigures() { return figures; }
    public void setFigures(List<GeometricFigure> figures) { this.figures = figures; }
}