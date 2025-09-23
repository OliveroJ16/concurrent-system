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
            // Verificar si ya se alcanzo el total antes de crear el producto
            if (cinta.todosProductosColocados()) {
                break;
            }

            int numero = switch (tipo) {
                case 1 -> random.nextInt(300);
                case 2 -> random.nextInt(300) + 300;
                default -> random.nextInt(401) + 600;
            };
            Producto producto = new Producto(numero);

            // Intenta colocar producto; si retorna false, ya se alcanzó el total
            if (!cinta.colocarProducto(producto)) {
                break;
            }
        }
    }
}