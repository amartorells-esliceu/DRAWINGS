package cat.liceu.drawings.models;

public class Triangle extends GeometricFigure {
    private int size;

    public Triangle() {
        super();
    }

    public Triangle(String name, String color, int x, int y, int size) {
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