package cat.liceu.drawings.repo;

import cat.liceu.drawings.models.Drawing;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DrawingRepoImpl implements DrawingRepo {

    private final List<Drawing> drawings = new ArrayList<>();

    @Override
    public void save(Drawing drawing) {
        Drawing existing = findById(drawing.getId());
        if (existing != null) {
            int index = drawings.indexOf(existing);
            drawings.set(index, drawing);
        } else {
            drawings.add(drawing);
        }
    }

    @Override
    public Drawing findById(String id) {
        if (id == null) return null;
        for (Drawing d : drawings) {
            if (id.equals(d.getId())) {
                return d;
            }
        }
        return null;
    }

    @Override
    public List<Drawing> findByUsername(String username) {
        List<Drawing> result = new ArrayList<>();
        if (username == null) return result;
        for (Drawing d : drawings) {
            if (username.equals(d.getOwnerUsername())) {
                result.add(d);
            }
        }
        return result;
    }

    @Override
    public List<Drawing> findAllExceptUsername(String username) {
        List<Drawing> result = new ArrayList<>();
        if (username == null) return result;
        for (Drawing d : drawings) {
            if (!username.equals(d.getOwnerUsername())) {
                result.add(d);
            }
        }
        return result;
    }

    @Override
    public void deleteByIdAndUsername(String id, String username) {
        if (id == null || username == null) return;

        Drawing drawingToDelete = null;
        for (Drawing d : drawings) {
            if (id.equals(d.getId()) && username.equals(d.getOwnerUsername())) {
                drawingToDelete = d;
                break;
            }
        }

        if (drawingToDelete != null) {
            drawings.remove(drawingToDelete);
        }
    }
}