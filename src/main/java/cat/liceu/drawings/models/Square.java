package cat.liceu.drawings.models;

public class Square extends GeometricFigure {
    private int size;

    public Square() {
        super();
    }

    public Square(String name, String color, int x, int y, int size) {
        super(name, color, x, y);
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}