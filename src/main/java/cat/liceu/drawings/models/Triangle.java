package cat.liceu.drawings.models;

public class Triangle extends GeometricFigures {
    private int mida;

    public Triangle() {
        super();
    }

    public Triangle(String nom, String color, int x, int y, int mida) {
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