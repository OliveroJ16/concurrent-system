import java.util.concurrent.atomic.AtomicInteger;

public class CadenaMontaje {
    private final Producto[] cinta;
    private final int capacidad;
    private final AtomicInteger totalAcomodados; 
    private final AtomicInteger totalEmpaquetados;
    private final int cantidadTotalProductos;
    private volatile boolean empaquetadoresPuedenTrabajar = false;

    public CadenaMontaje(int capacidad, int cantidadTotalProductos) {
        this.capacidad = capacidad;
        this.cinta = new Producto[capacidad];
        this.totalAcomodados = new AtomicInteger(0);
        this.totalEmpaquetados = new AtomicInteger(0);
        this.cantidadTotalProductos = cantidadTotalProductos;
    }

    public boolean colocarProducto(Producto producto) {
        synchronized (cinta) { // Solo un hilo puede acceder a la cinta
            if (totalAcomodados.get() >= cantidadTotalProductos) {
                return false;
            }

            // Buscar una posicion vacia en la cinta
            for (int i = 0; i < capacidad; i++) {
                if (cinta[i] == null && totalAcomodados.get() < cantidadTotalProductos) {
                    cinta[i] = producto;

                    if (totalAcomodados.incrementAndGet() > cantidadTotalProductos) {
                        cinta[i] = null; // revertir si se pasa del límite
                        totalAcomodados.decrementAndGet();
                        return false;
                    }

                    // Verificar si la cinta está llena para habilitar empaquetadores
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
            return false; // No hay espacio disponible
        }
    }

    public Producto retirarProducto(int tipo) {
        synchronized (cinta) { // Solo un hilo puede acceder a la cinta
            if (!empaquetadoresPuedenTrabajar) {
                return null;
            }

            // Buscar un producto del tipo solicitado
            for (int i = 0; i < capacidad; i++) {
                if (cinta[i] != null && cinta[i].getTipo() == tipo) {
                    Producto producto = cinta[i];
                    cinta[i] = null;
                    totalEmpaquetados.incrementAndGet();
                    return producto;
                }
            }
            return null; // No se encontro producto del tipo solicitado
        }
    }

    public int getTotalAcomodados() {
        return totalAcomodados.get();
    }

    public int getTotalEmpaquetados() {
        return totalEmpaquetados.get();
    }

    public boolean estaVacia() {
        synchronized (cinta) {
            for (int i = 0; i < capacidad; i++) {
                if (cinta[i] != null) {
                    return false;
                }
            }
            return true;
        }
    }

    public int getCantidadTotal() {
        return cantidadTotalProductos;
    }

    public boolean todosProductosColocados() {
        return totalAcomodados.get() >= cantidadTotalProductos;
    }
}