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

    private String getUsername(HttpSession session) {
        return (String) session.getAttribute("username");
    }

    @GetMapping("/editor")
    public String editor(@RequestParam(required = false) String id, HttpSession session, Model model) {
        String username = getUsername(session);
        if (username == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", username);
        model.addAttribute("canEdit", true);
        if (id != null) {
            Drawing drawing = drawingService.getDrawingById(id);
            if (drawing == null) {
                return "redirect:/drawings/history";
            }
            model.addAttribute("drawing", drawing);
            model.addAttribute("canEdit", username.equals(drawing.getOwnerUsername()));
        }

        return "editor";
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveDrawing(@RequestBody DrawingDTO request, HttpSession session) {
        String username = getUsername(session);

        if (username == null){
            return ResponseEntity.status(401).body("No autoritzat");
        }

        Drawing savedDrawing = drawingService.saveDrawing(request.getId(), request.getName(), username, request.getFigures());

        if (savedDrawing == null) {
            return ResponseEntity.status(403).body("Accés denegat o dibuix no trobat");
        }

        return ResponseEntity.ok(savedDrawing);
    }

    @GetMapping("/history")
    public String history(HttpSession session, Model model) {
        String username = getUsername(session);
        if (username == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", username);
        model.addAttribute("myDrawings", drawingService.getMyDrawings(username));
        model.addAttribute("othersDrawings", drawingService.getOthersDrawings(username));
        return "history";
    }

    @PostMapping("/delete")
    public String deleteDrawing(@RequestParam String id, HttpSession session) {
        String username = getUsername(session);
        if (username == null) {
            return "redirect:/login";
        }

        drawingService.deleteDrawing(id, username);
        return "redirect:/drawings/history";
    }
}