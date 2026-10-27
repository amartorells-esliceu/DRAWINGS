package cat.liceu.drawings.repo;

import cat.liceu.drawings.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepoImpl implements UserRepo {

    private final List<User> users = new ArrayList<>();

    public UserRepoImpl() {
        User u1 = new User();
        u1.setName("Administrador");
        u1.setUsername("admin");
        u1.setPassword("123456");
        users.add(u1);

        User u2 = new User();
        u2.setName("Andreu Martorell");
        u2.setUsername("Andreu");
        u2.setPassword("654321");
        users.add(u2);
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

    @Override
    public void save(User user) {
        users.add(user);
    }
}