package cat.liceu.drawings.repo;

import cat.liceu.drawings.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepoImpl implements UserRepo {

    private final List<User> users = new ArrayList<>();

    public UserRepoImpl() {
        // Usuari de prova
        User u = new User();
        u.setUsername("admin");
        u.setPassword("123456");
        users.add(u);
    }

    @Override
    public User findByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
}