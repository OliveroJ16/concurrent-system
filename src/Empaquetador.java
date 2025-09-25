import java.util.List;

public class Empaquetador extends Thread {
    private CadenaMontaje cinta;
    private int tipo; // tipo de producto que maneja
    private List<Producto> contenedor; // vector compartido para este tipo
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
                // Agrega el producto al contenedor compartido de su tipo
                synchronized (contenedor) {
                    contenedor.add(producto);
                }
            } else {
                // Si no hay producto de su tipo, verificar si debe terminar
                if (colocadoresTerminaron && cinta.estaVacia()) {
                    break;
                }
            }
        }
    }
}