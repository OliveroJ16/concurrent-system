import java.util.LinkedList;
import java.util.Queue;

public class CadenaMontaje {
    private Queue<Producto> cinta;
    private int capacidad;
    private int totalAcomodados;
    private int totalEmpaquetados;

    public CadenaMontaje(int capacidad) {
        this.capacidad = capacidad;
        this.cinta = new LinkedList<>();
        this.totalAcomodados = 0;
        this.totalEmpaquetados = 0;
    }
}
