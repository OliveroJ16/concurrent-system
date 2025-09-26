import java.util.List;

public class Empaquetador extends Thread {
    private CadenaMontaje cinta;
    private int tipo;
    private List<Producto> contenedor;
    private volatile boolean colocadoresTerminaron;

    public Empaquetador(int tipo, CadenaMontaje cinta, List<Producto> contenedor) {
        this.tipo = tipo;
        this.cinta = cinta;
        this.contenedor = contenedor;
        this.colocadoresTerminaron = false;
    }

    public void setColocadoresTerminaron(boolean terminado) {
        this.colocadoresTerminaron = terminado;
    }

    @Override
    public void run() {

        while (true) {
            Producto producto = cinta.retirarProducto(tipo);

            if (producto != null) {
                synchronized (contenedor) {
                    contenedor.add(producto);
                }
            } else {
                if (colocadoresTerminaron && cinta.estaVacia()) {
                    break;
                }
            }
        }
    }
}
