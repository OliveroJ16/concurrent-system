import java.util.Random;

public class Colocador extends Thread {
    private final CadenaMontaje cinta;
    private final int tipo; // tipo del colocador

    public Colocador(int tipo, CadenaMontaje cinta) {
        this.cinta = cinta;
        this.tipo = tipo;
    }

    @Override
    public void run() {
        Random random = new Random();

        while (true) {
            // Verificar si ya se alcanzó el total antes de crear el producto
            if (cinta.todosProductosColocados()) {
                break;
            }

            int numero = switch (tipo) {
                case 1 -> random.nextInt(300);
                case 2 -> random.nextInt(300) + 300;
                default -> random.nextInt(401) + 600;
            };
            Producto producto = new Producto(numero);

            // Intentar colocar producto continuamente hasta lograrlo o hasta que se alcance el total
            boolean colocado = false;
            while (!colocado && !cinta.todosProductosColocados()) {
                colocado = cinta.colocarProducto(producto);
                
                if (!colocado) {
                    // Pequeña pausa para evitar consumo excesivo de CPU
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            // Si ya se alcanzó el total mientras intentaba colocar, salir
            if (cinta.todosProductosColocados()) {
                break;
            }
        }
    }
}