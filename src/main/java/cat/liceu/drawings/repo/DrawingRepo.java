package cat.liceu.drawings.repo;

import cat.liceu.drawings.models.Drawing;
import java.util.List;

public interface DrawingRepo {
    void save(Drawing drawing);
    Drawing findById(String id);
    List<Drawing> findByUsername(String username);
    List<Drawing> findAllExceptUsername(String username);
    void deleteByIdAndUsername(String id, String username);
}