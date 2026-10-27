package cat.liceu.drawings.models;

import java.util.ArrayList;
import java.util.List;

public class Freehand extends GeometricFigures {

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

    private List<Point> punts = new ArrayList<>();

    public Freehand() {
        super();
    }

    public Freehand(String nom, String color, int x, int y, List<Point> punts) {
        super(nom, color, x, y);
        this.punts = punts;
    }

    public List<Point> getPunts() {
        return punts;
    }

    public void setPunts(List<Point> punts) {
        this.punts = punts;
    }

    public void addPunt(int x, int y) {
        this.punts.add(new Point(x, y));
    }
}