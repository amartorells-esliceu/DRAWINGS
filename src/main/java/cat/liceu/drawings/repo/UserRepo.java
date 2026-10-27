package cat.liceu.drawings.repo;

import cat.liceu.drawings.models.User;

public interface UserRepo {
    User findByUsername(String username);
    void save(User user);
}