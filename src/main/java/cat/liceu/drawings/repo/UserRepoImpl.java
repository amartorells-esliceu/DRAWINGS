package cat.liceu.drawings.repo;

import cat.liceu.drawings.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepoImpl implements UserRepo {

    private final List<User> users = new ArrayList<>();

    @Override
    public User findByUsername(String username) {
        for (User u : users){
            if (username.equals(u.getUsername())){
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
