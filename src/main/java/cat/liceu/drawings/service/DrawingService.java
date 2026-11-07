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

    // 1. Guardar o actualitzar un dibuix
    public Drawing saveDrawing(String id, String nom, String username, List<GeometricFigure> figures) {
        // TODO: Comprovar si el nom ve buit. Si està buit, generar un nom aleatori (ex: "Dibuix-4829").

        // TODO: Si l'ID NO ve buit (és una edició):
        //       - Cerca el dibuix existent al repositori pel seu ID.
        //       - Comprova que existeixi i que el seu propietari sigui l'usuari actual en sessió.
        //       - Actualitza el seu nom, la seva llista de figures i la data de modificació (LocalDateTime.now()).
        updateDrawing(id, nom, username, figures);

        // TODO: Si l'ID VE buit (és un dibuix nou):
        //       - Genera un nou ID únic (p. ex. UUID.randomUUID().toString()).
        //       - Instancia un nou objecte Drawing amb l'ID, el nom, l'usuari i les figures.
        createDrawing(id, nom, username, figures);

        // TODO: Envia el dibuix al repositori (drawingRepo.save) per desar-lo en memòria i retorna l'objecte Drawing.
        return null;
    }

    // TODO: Si l'ID NO ve buit (és una edició):
    //       - Cerca el dibuix existent al repositori pel seu ID.
    //       - Comprova que existeixi i que el seu propietari sigui l'usuari actual en sessió.
    //       - Actualitza el seu nom, la seva llista de figures i la data de modificació (LocalDateTime.now()).
    private void updateDrawing(String id, String nom, String username, List<GeometricFigure> figures){
        Drawing dibuixExistent = drawingRepo.findById(id);

        if(dibuixExistent != null){

            dibuixExistent.setName(nom);
            dibuixExistent.setFigures(figures);
            dibuixExistent.setUpdatedAt(LocalDateTime.now());
            drawingRepo.save(dibuixExistent);
        }
    }

    // TODO: Si l'ID VE buit (és un dibuix nou):
    //       - Genera un nou ID únic (p. ex. UUID.randomUUID().toString()).
    //       - Instancia un nou objecte Drawing amb l'ID, el nom, l'usuari i les figures.
    private void createDrawing(String id, String nom, String username, List<GeometricFigure> figures){
        //Genera un nou ID únic

        //Crear nova instancia
        Drawing dibuix = new Drawing(id, nom, username, figures); //Actualitza dates

        //Cridar al repositori
        drawingRepo.save(dibuix);
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