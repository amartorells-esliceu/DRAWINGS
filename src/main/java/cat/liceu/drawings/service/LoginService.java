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
}