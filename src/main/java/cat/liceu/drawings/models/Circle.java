package cat.liceu.drawings.models;

public class Circle extends GeometricFigure {
    private int radius;

    public Circle() {
        super();
    }

    public Circle(String name, String color, int x, int y, int radius) {
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