import java.util.ArrayList;
import java.util.List;

public class CadenaMontaje {
    private List<Producto> cinta;
    private int capacidad;
    private int totalAcomodados;
    private int totalEmpaquetados;
    private int cantidadTotalProductos;

    public CadenaMontaje(int capacidad, int cantidadTotalProductos) {
        this.capacidad = capacidad;
        this.cinta = new ArrayList<>();
        this.totalAcomodados = 0;
        this.totalEmpaquetados = 0;
        this.cantidadTotalProductos = cantidadTotalProductos;
    }

    public synchronized boolean colocarProducto(Producto producto) {
        // Verificar si ya se alcanzó la cantidad total ANTES de colocar
        if (totalAcomodados >= cantidadTotalProductos) {
            notifyAll(); // despertar empaquetadores que puedan estar esperando
            return false;
        }

        // Esperar si la cinta está llena
        while (cinta.size() >= capacidad && totalAcomodados < cantidadTotalProductos) {
            try {
                wait(); //se pone en espera hasta que haya espacio (hilo actual)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        /**
         * Verificar nuevamente despues de wait() (doble verificacion)
         * Correccion aqui para evitar sobrepasar el total*/
        if (totalAcomodados >= cantidadTotalProductos) {
            notifyAll(); // despertar empaquetadores para que empiecen a retirar
            return false;
        }

        // Colocar producto
        cinta.add(producto);
        totalAcomodados++;
        notifyAll(); // despertar empaquetadores
        return true;
    }

    public synchronized Producto retirarProducto(int tipo) {
        for (int i = 0; i < cinta.size(); i++) {
            Producto proceso = cinta.get(i);
            if (proceso.getTipo() == tipo) {
                cinta.remove(i);        // Retira el producto de la cinta
                totalEmpaquetados++;    // Actualiza contador
                notifyAll();            // Notifica a los colocadores que hay espacio
                return proceso;               // Devuelve el producto retirado
            }
        }
        return null; // No hay productos de ese tipo en la cinta
    }

    public synchronized int getTotalAcomodados() {
        return this.totalAcomodados;
    }

    public synchronized int getTotalEmpaquetados() {
        return this.totalEmpaquetados;
    }

    public synchronized boolean estaVacia() {
        return cinta.isEmpty();
    }

    public int getCantidadTotal() {
        return this.cantidadTotalProductos;
    }

    public synchronized boolean todosProductosColocados() {
        return totalAcomodados >= cantidadTotalProductos;
    }
}