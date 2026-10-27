package cat.liceu.drawings.service;

import cat.liceu.drawings.models.User;
import cat.liceu.drawings.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    UserRepo userRepo;

    public boolean checkLogin(String username, String password) {
        User u = userRepo.findByUsername(username);

        if (u == null) return false;

        return password.equals(u.getPassword());
    }

    public boolean registerUser(String name, String username, String password) {
        if (userRepo.findByUsername(username) != null) {
            return false;
        }

        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        userRepo.save(user);
        return true;
    }
}