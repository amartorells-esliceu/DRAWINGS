package cat.liceu.drawings.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginForm {

    @NotBlank(message = "L'usuari és obligatori")
    @Size(min = 5, max = 10, message = "Ha de ser entre 5 i 10 characters")
    private String username;

    @NotBlank(message = "La contrasenya és obligatòria")
    @Size(min = 2, max = 10, message = "Ha de ser entre 2 i 10 characters")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}