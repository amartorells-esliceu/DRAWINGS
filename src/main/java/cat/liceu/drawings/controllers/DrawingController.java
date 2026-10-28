package cat.liceu.drawings.controllers;

import cat.liceu.drawings.dto.DrawingDTO;
import cat.liceu.drawings.models.Drawing;
import cat.liceu.drawings.models.GeometricFigure;
import cat.liceu.drawings.service.DrawingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/drawings")
public class DrawingController {

    @Autowired
    private DrawingService drawingService;

    // 1. Obrir la pantalla de l'editor de dibuixos (per crear-ne un de nou o carregar-ne un d'existent)
    @GetMapping("/editor")
    public String editor(@RequestParam(required = false) String id, HttpSession session, Model model) {
        // TODO: Comprovar si l'usuari té sessió activa (session.getAttribute("username")).
        //       - Si és null, redirigir a la pàgina de login ("redirect:/login").

        // TODO: Afegir el nom d'usuari en sessió al Model (model.addAttribute("username", username)).

        // TODO: Si ve un 'id' per paràmetre (no és null), cercar el dibuix amb drawingService.getDrawingById(id)
        //       i afegir-lo al Model (model.addAttribute("drawing", d)) per carregar-lo a la plantilla.

        // TODO: Retornar el nom de la plantilla HTML de l'editor ("editor").
        return null;
    }

    // 2. Endpoint REST per a l'auto-guardat automàtic des de JavaScript (AJAX)
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<?> saveDrawing(@RequestBody DrawingDTO request, HttpSession session) {
        // TODO: Comprovar si l'usuari té la sessió activa. Si no en té, retornar un ResponseEntity.status(401).body("No autoritzat").

        // TODO: Cridar a drawingService.saveDrawing(...) passant l'ID, el nom del dibuix, l'usuari en sessió i la llista de figures.

        // TODO: Retornar ResponseEntity.ok(drawingGuardat) amb l'objecte de dibuix actualitzat.
        return null;
    }

    // 3. Carregar la pàgina de l'historial de dibuixos (propis i de la resta d'usuaris)
    @GetMapping("/history")
    public String history(HttpSession session, Model model) {
        // TODO: Comprovar la sessió de l'usuari. Si no està autenticat, redirigir a /login.

        // TODO: Obtenir els dibuixos propis amb drawingService.getMyDrawings(username) i afegir-los al Model ("myDrawings").

        // TODO: Obtenir els dibuixos d'altres usuaris amb drawingService.getOthersDrawings(username) i afegir-los al Model ("othersDrawings").

        // TODO: Retornar el nom de la plantilla HTML de l'historial ("history").
        return null;
    }

    // 4. Petició per esborrar un dibuix de l'historial propi
    @PostMapping("/delete")
    public String deleteDrawing(@RequestParam String id, HttpSession session) {
        // TODO: Comprovar si l'usuari està autenticat a la sessió. Si no, redirigir a /login.

        // TODO: Executar la baixada amb drawingService.deleteDrawing(id, username).

        // TODO: Redirigir de nou a la taula de l'historial ("redirect:/drawings/history").
        return null;
    }
}