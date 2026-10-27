package cat.liceu.drawings.models;

import java.util.ArrayList;
import java.util.List;

public class Freehand extends GeometricFigure {

    public static class Point {
        private int x;
        private int y;

        public Point() {
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    private List<Point> points = new ArrayList<>();

    public Freehand() {
        super();
    }

    public Freehand(String name, String color, int x, int y, List<Point> points) {
        super(name, color, x, y);
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void addPoint(int x, int y) {
        this.points.add(new Point(x, y));
    }
}