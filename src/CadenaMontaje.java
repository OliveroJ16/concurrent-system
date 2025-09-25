public class CadenaMontaje {
    private Producto[] cinta;
    private int capacidad;
    private int totalAcomodados;
    private int totalEmpaquetados;
    private int cantidadTotalProductos;

    public CadenaMontaje(int capacidad, int cantidadTotalProductos) {
        this.capacidad = capacidad;
        this.cinta = new Producto[capacidad];
        this.totalAcomodados = 0;
        this.totalEmpaquetados = 0;
        this.cantidadTotalProductos = cantidadTotalProductos;
    }

    public synchronized boolean colocarProducto(Producto producto) {
        // Verificar si ya se alcanzó la cantidad total
        if (totalAcomodados >= cantidadTotalProductos) {
            return false;
        }

        // Buscar un espacio libre en el array (posición == null)
        for (int i = 0; i < capacidad; i++) {
            if (cinta[i] == null) {
                cinta[i] = producto;
                totalAcomodados++;
                return true;
            }
        }
        
        return false; // No hay espacios libres
    }

    public synchronized Producto retirarProducto(int tipo) {
        for (int i = 0; i < capacidad; i++) {
            if (cinta[i] != null && cinta[i].getTipo() == tipo) {
                Producto producto = cinta[i];
                cinta[i] = null;        // Libera el espacio
                totalEmpaquetados++;    // Actualiza contador
                return producto;        // Devuelve el producto retirado
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
        for (int i = 0; i < capacidad; i++) {
            if (cinta[i] != null) {
                return false;
            }
        }
        return true;
    }

    public int getCantidadTotal() {
        return this.cantidadTotalProductos;
    }

    public synchronized boolean todosProductosColocados() {
        return totalAcomodados >= cantidadTotalProductos;
    }
}