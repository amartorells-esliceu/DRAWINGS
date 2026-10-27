package cat.liceu.drawings.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterForm {

    @NotBlank(message = "El nom és obligatori")
    @Size(max = 50, message = "El nom no pot tenir més de 50 caràcters")
    private String name;

    @NotBlank(message = "L'usuari és obligatori")
    @Size(min = 2, max = 10, message = "Ha de ser entre 2 i 10 caràcters")
    private String username;

    @NotBlank(message = "La contrasenya és obligatòria")
    @Size(min = 5, max = 20, message = "Ha de ser de com a mínim 5 caràcters")
    private String password;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}