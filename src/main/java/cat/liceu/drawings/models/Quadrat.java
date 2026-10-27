package cat.liceu.drawings.models;

public class Quadrat extends GeometricFigures {
    private int mida;

    public Quadrat() {
        super();
    }

    public Quadrat(String nom, String color, int x, int y, int mida) {
        super(nom, color, x, y);
        this.mida = mida;
    }

    public int getMida() {
        return mida;
    }

    public void setMida(int mida) {
        this.mida = mida;
    }
}