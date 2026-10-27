package cat.liceu.drawings.models;

public class Estrella extends GeometricFigures {
    private int radi;

    public Estrella() {
        super();
    }

    public Estrella(String nom, String color, int x, int y, int radi) {
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