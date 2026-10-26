package cat.liceu.drawings.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CookieController {
    @GetMapping("/preferencies")
    public String guardarPreferencia(@RequestParam String tema, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("tema", tema)
                .maxAge(60 * 60 * 24 * 30) // 30 dies, en segons
                .path("/")
                .httpOnly(true)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return "redirect:/";
    }

    @GetMapping("/")
    public String inici(@CookieValue(name = "tema", defaultValue = "clar") String tema, Model model) {
        model.addAttribute("tema", tema);
        return "inici";
    }
}