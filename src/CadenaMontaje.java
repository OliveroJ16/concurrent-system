import java.util.concurrent.atomic.AtomicInteger;

public class CadenaMontaje {
    private final Producto[] cinta;
    private final Object[] locks;
    private final int capacidad;
    private final AtomicInteger totalAcomodados; 
    private final AtomicInteger totalEmpaquetados;
    private final int cantidadTotalProductos;
    private volatile boolean empaquetadoresPuedenTrabajar = false;

    public CadenaMontaje(int capacidad, int cantidadTotalProductos) {
        this.capacidad = capacidad;
        this.cinta = new Producto[capacidad];
        this.locks = new Object[capacidad];
        for (int i = 0; i < capacidad; i++) {
            locks[i] = new Object();
        }
        this.totalAcomodados = new AtomicInteger(0);
        this.totalEmpaquetados = new AtomicInteger(0);
        this.cantidadTotalProductos = cantidadTotalProductos;
    }

    public boolean colocarProducto(Producto producto) {
        if (totalAcomodados.get() >= cantidadTotalProductos) {
            return false;
        }

        for (int i = 0; i < capacidad; i++) {
            synchronized (locks[i]) {
                if (cinta[i] == null && totalAcomodados.get() < cantidadTotalProductos) {
                    cinta[i] = producto;

                    if (totalAcomodados.incrementAndGet() > cantidadTotalProductos) {
                        cinta[i] = null; // revertir si se pasa del limite
                        return false;
                    }

                    if (!empaquetadoresPuedenTrabajar) {
                        boolean llena = true;
                        for (int j = 0; j < capacidad; j++) {
                            if (cinta[j] == null) {
                                llena = false;
                                break;
                            }
                        }
                        if (llena) {
                            empaquetadoresPuedenTrabajar = true;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public Producto retirarProducto(int tipo) {
        if (!empaquetadoresPuedenTrabajar) {
            return null;
        }

        for (int i = 0; i < capacidad; i++) {
            synchronized (locks[i]) {
                if (cinta[i] != null && cinta[i].getTipo() == tipo) {
                    Producto producto = cinta[i];
                    cinta[i] = null;
                    totalEmpaquetados.incrementAndGet();
                    return producto;
                }
            }
        }
        return null;
    }

    public int getTotalAcomodados() {
        return totalAcomodados.get();
    }

    public int getTotalEmpaquetados() {
        return totalEmpaquetados.get();
    }

    public boolean estaVacia() {
        for (int i = 0; i < capacidad; i++) {
            synchronized (locks[i]) {
                if (cinta[i] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getCantidadTotal() {
        return cantidadTotalProductos;
    }

    public boolean todosProductosColocados() {
        return totalAcomodados.get() >= cantidadTotalProductos;
    }
}
