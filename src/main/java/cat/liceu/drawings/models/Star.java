package cat.liceu.drawings.models;

public class Star extends GeometricFigure {
    private int radius;

    public Star() {
        super();
    }

    public Star(String name, String color, int x, int y, int radius) {
        super(name, color, x, y);
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}