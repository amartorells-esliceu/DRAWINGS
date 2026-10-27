package cat.liceu.drawings.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Drawing {
    private String id;
    private String nom;
    private String usernamePropietari;
    private List<GeometricFigure> figures = new ArrayList<>();
    private LocalDateTime dataCreacio;
    private LocalDateTime dataModificacio;

    public Drawing() {
    }

    public Drawing(String id, String nom, String usernamePropietari, List<GeometricFigure> figures) {
        this.id = id;
        this.nom = nom;
        this.usernamePropietari = usernamePropietari;
        this.figures = figures;
        this.dataCreacio = LocalDateTime.now();
        this.dataModificacio = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUsernamePropietari() {
        return usernamePropietari;
    }

    public void setUsernamePropietari(String usernamePropietari) {
        this.usernamePropietari = usernamePropietari;
    }

    public List<GeometricFigure> getFigures() {
        return figures;
    }

    public void setFigures(List<GeometricFigure> figures) {
        this.figures = figures;
    }

    public LocalDateTime getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(LocalDateTime dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public LocalDateTime getDataModificacio() {
        return dataModificacio;
    }

    public void setDataModificacio(LocalDateTime dataModificacio) {
        this.dataModificacio = dataModificacio;
    }
}