package cat.liceu.drawings.service;

import cat.liceu.drawings.models.Drawing;
import cat.liceu.drawings.models.GeometricFigure;
import cat.liceu.drawings.repo.DrawingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        // TODO: Si l'ID VE buit (és un dibuix nou):
        //       - Genera un nou ID únic (p. ex. UUID.randomUUID().toString()).
        //       - Instancia un nou objecte Drawing amb l'ID, el nom, l'usuari i les figures.

        // TODO: Envia el dibuix al repositori (drawingRepo.save) per desar-lo en memòria i retorna l'objecte Drawing.
        return null;
    }

    // 2. Obtenir l'historial de dibuixos propis
    public List<Drawing> getMyDrawings(String username) {
        // TODO: Demana al repositori tots els dibuixos que pertanyen a l'usuari autenticat.
        return null;
    }

    // 3. Obtenir l'historial de dibuixos de la resta d'usuaris
    public List<Drawing> getOthersDrawings(String username) {
        // TODO: Demana al repositori tots els dibuixos que NO pertanyen a l'usuari en sessió.
        return null;
    }

    // 4. Obtenir un dibuix concret pel seu ID (per carregar-lo a l'editor visual)
    public Drawing getDrawingById(String id) {
        // TODO: Cerca al repositori el dibuix que coincideix amb aquest ID i el retorna.
        return null;
    }

    // 5. Esborrar un dibuix de l'historial propi
    public boolean deleteDrawing(String id, String username) {
        // TODO: Comprova si el dibuix existeix i pertany a l'usuari en sessió.
        // TODO: Si és així, crida al repositori per eliminar-lo i retorna true.
        // TODO: Si no pertany a l'usuari o no existeix, no el remagatzemis/eliminis i retorna false.
        return false;
    }
}