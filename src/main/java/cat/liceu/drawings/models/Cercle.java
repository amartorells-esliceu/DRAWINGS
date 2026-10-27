package cat.liceu.drawings.models;

public class Cercle extends GeometricFigures {
    private int radi;

    public Cercle() {
        super();
    }

    public Cercle(String nom, String color, int x, int y, int radi) {
        super(nom, color, x, y);
        this.radi = radi;
    }

    public int getRadi() {
        return radi;
    }

    public void setRadi(int radi) {
        this.radi = radi;
    }
}