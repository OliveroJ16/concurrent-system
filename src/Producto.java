public class Producto {
    private int numeroTipo;

    public Producto(int numeroTipo) {
        this.numeroTipo = numeroTipo;
    }

    public int getTipo() {
        if (numeroTipo < 300)
            return 1;
        else if (numeroTipo < 600)
            return 2;
        else
            return 3;
    }
}
