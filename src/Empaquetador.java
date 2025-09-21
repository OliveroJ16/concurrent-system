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
        // Despertar a todos los empaquetadores cuando se notifica que terminaron
        synchronized (cinta) {
            cinta.notifyAll();
        }
    }

    @Override
    public void run() {
        while (true) {
            Producto p;
            synchronized (cinta) {
                // Espera mientras no haya producto de su tipo
                while ((p = cinta.retirarProducto(tipo)) == null) {
                    // Si los colocadores terminaron y la cinta está vacía, termina
                    if (colocadoresTerminaron && cinta.estaVacia()) {
                        return;
                    }
                    try {
                        cinta.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                // Producto retirado, notifica a los colocadores
                cinta.notifyAll();
            }

            // Agrega el producto al contenedor compartido de su tipo
            synchronized (contenedor) {
                contenedor.add(p);
            }
        }
    }
}