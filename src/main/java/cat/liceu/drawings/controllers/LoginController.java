package cat.liceu.drawings.controllers;

import cat.liceu.drawings.form.LoginForm;
import cat.liceu.drawings.service.LoginService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        }

        boolean registered = loginService.registerUser(loginForm.getUsername(), loginForm.getPassword());
        if (!registered) {
            model.addAttribute("message", "Aquest usuari ja existeix");
            return "register";
        }

        redirectAttributes.addFlashAttribute("message", "Usuari registrat correctament. Ja pots iniciar sessió.");
        return "redirect:/login";
    }

    @GetMapping({"/", "/login"})
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                            BindingResult result,
                            Model model,
                            HttpSession session) {

        if (result.hasErrors()) {
            return "login";
        }

        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        if (loginService.checkLogin(username, password)) {
            System.out.printf("L'usuari %s ha iniciat sessió correctament.%n", username);
            session.setAttribute("username", username);
            return "redirect:/main";
        }

        model.addAttribute("message", "User/pass incorrect");
        return "login";
    }

    @GetMapping("/main")
    public String privatePage(HttpSession session) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/login";
        }
        return "main";
    }
}