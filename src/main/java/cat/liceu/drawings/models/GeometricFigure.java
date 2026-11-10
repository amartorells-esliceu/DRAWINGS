package cat.liceu.drawings.models;

import java.util.ArrayList;
import java.util.List;

public class GeometricFigure {

    public static class Point {
        private int x;
        private int y;

        public Point() {}
        public Point(int x, int y) { this.x = x; this.y = y; }
        public int getX() { return x; }
        public void setX(int x) { this.x = x; }
        public int getY() { return y; }
        public void setY(int y) { this.y = y; }
    }

    private String name;
    private String type;
    private String color;
    private int x;
    private int y;
    private int size;
    private List<Point> points = new ArrayList<>();

    public GeometricFigure() {
    }

    public GeometricFigure(String name, String type, String color, int x, int y) {
        this.name = name;
        this.type = type;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public List<Point> getPoints() { return points; }
    public void setPoints(List<Point> points) { this.points = points; }
}