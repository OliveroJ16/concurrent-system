import java.util.concurrent.locks.ReentrantLock;

public class CadenaMontaje {
    private volatile Producto[] cinta;
    private volatile ReentrantLock[] locks;   // un lock por posicion
    private final ReentrantLock contadorLock = new ReentrantLock(); // Lock especifico para totalAcomodados
    private final int capacidad;
    private volatile int totalAcomodados;
    private volatile int totalEmpaquetados;
    private final int cantidadTotalProductos;
    private volatile boolean empaquetadoresPuedenTrabajar = false; // Se activa cuando la cinta se llena por primera vez

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
        this.empaquetadoresPuedenTrabajar = false;
    }

    public boolean colocarProducto(Producto producto) {
        // Primera verificación rapida sin lock
        if (totalAcomodados >= cantidadTotalProductos) {
            return false;
        }

        for (int i = 0; i < capacidad; i++) {
            if (locks[i].tryLock()) { // intenta bloquear la posición
                try {
                    // Segunda verificación dentro del lock para evitar race condition
                    if (cinta[i] == null && totalAcomodados < cantidadTotalProductos) {
                        cinta[i] = producto;
                        
                        // Usar lock específico para actualizar totalAcomodados
                        contadorLock.lock();
                        try {
                            //Volver a verificar
                            if (totalAcomodados < cantidadTotalProductos) {
                                totalAcomodados++;
                            } else {
                                // Si se alcanzo el linmite, revertir la colocación
                                cinta[i] = null;
                                return false;
                            }
                        } finally {
                            contadorLock.unlock();
                        }
                        
                        // Verificar si la cinta esta completamente llena para activar empaquetadores
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
                } finally {
                    locks[i].unlock(); // siempre liberar el lock
                }
            }
        }
        return false; // no hay posiciones libres disponibles
    }

    public Producto retirarProducto(int tipo) {
        // Solo permitir retirar si los empaquetadores ya pueden trabajar
        if (!empaquetadoresPuedenTrabajar) {
            return null;
        }
        
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