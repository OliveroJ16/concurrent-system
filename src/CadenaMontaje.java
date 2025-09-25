import java.util.concurrent.locks.ReentrantLock;

public class CadenaMontaje {
    private volatile Producto[] cinta;
    private volatile ReentrantLock[] locks;   // un lock por posición
    private final int capacidad;
    private volatile int totalAcomodados;
    private volatile int totalEmpaquetados;
    private final int cantidadTotalProductos;

    public CadenaMontaje(int capacidad, int cantidadTotalProductos) {
        this.capacidad = capacidad;
        this.cinta = new Producto[capacidad];
        this.locks = new ReentrantLock[capacidad];
        for (int i = 0; i < capacidad; i++) {
            locks[i] = new ReentrantLock();
        }
        this.totalAcomodados = 0;
        this.totalEmpaquetados = 0;
        this.cantidadTotalProductos = cantidadTotalProductos;
    }

    public boolean colocarProducto(Producto producto) {
        if (totalAcomodados >= cantidadTotalProductos) {
            return false;
        }

        for (int i = 0; i < capacidad; i++) {
            if (locks[i].tryLock()) { // intenta bloquear la posición
                try {
                    if (cinta[i] == null && totalAcomodados < cantidadTotalProductos) {
                        cinta[i] = producto;
                        totalAcomodados++;
                        return true;
                    }
                } finally {
                    locks[i].unlock(); // siempre libera el lock
                }
            }
        }
        return false; // no hay posiciones libres disponibles
    }

    public Producto retirarProducto(int tipo) {
        for (int i = 0; i < capacidad; i++) {
            if (locks[i].tryLock()) {
                try {
                    if (cinta[i] != null && cinta[i].getTipo() == tipo) {
                        Producto producto = cinta[i];
                        cinta[i] = null;
                        totalEmpaquetados++;
                        return producto;
                    }
                } finally {
                    locks[i].unlock();
                }
            }
        }
        return null; // no hay productos del tipo buscado
    }

    public synchronized int getTotalAcomodados() {
        return totalAcomodados;
    }

    public synchronized int getTotalEmpaquetados() {
        return totalEmpaquetados;
    }

    public boolean estaVacia() {
        for (int i = 0; i < capacidad; i++) {
            locks[i].lock();
            try {
                if (cinta[i] != null) {
                    return false;
                }
            } finally {
                locks[i].unlock();
            }
        }
        return true;
    }

    public int getCantidadTotal() {
        return cantidadTotalProductos;
    }

    public boolean todosProductosColocados() {
        return totalAcomodados >= cantidadTotalProductos;
    }
}
