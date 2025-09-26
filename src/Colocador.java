import java.util.Random;

public class Colocador extends Thread {
    private final CadenaMontaje cinta;
    private final int tipo;

    public Colocador(int tipo, CadenaMontaje cinta) {
        this.cinta = cinta;
        this.tipo = tipo;
    }

    @Override
    public void run() {
        Random random = new Random();
        boolean terminarHilo = true;

        while (terminarHilo) {
            if (cinta.todosProductosColocados()) {
                terminarHilo = false;
            } else {
                int numero = switch (tipo) {
                    case 1 -> random.nextInt(300);
                    case 2 -> random.nextInt(300) + 300;
                    default -> random.nextInt(401) + 600;
                };
                Producto producto = new Producto(numero);

                boolean colocado = false;
                while (!colocado && !cinta.todosProductosColocados()) {
                    colocado = cinta.colocarProducto(producto);
                }

                if (cinta.todosProductosColocados()) {
                    terminarHilo = false;
                }
            }
        }
    }
}
