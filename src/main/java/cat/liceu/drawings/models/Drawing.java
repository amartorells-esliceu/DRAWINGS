package cat.liceu.drawings.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Drawing {
    private String id;
    private String name;
    private String ownerUsername;
    private List<GeometricFigure> figures = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Drawing() {
    }

    public Drawing(String id, String name, String ownerUsername, List<GeometricFigure> figures) {
        this.id = id;
        this.name = name;
        this.ownerUsername = ownerUsername;
        this.figures = figures;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public List<GeometricFigure> getFigures() {
        return figures;
    }

    public void setFigures(List<GeometricFigure> figures) {
        this.figures = figures;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}