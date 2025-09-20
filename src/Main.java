import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        /*
         * “¿Los productos deben generarse todos antes en una lista 
         * o cada colocador puede generarlos aleatoriamente mientras los coloca en la cinta?”
         */
        Scanner scanner = new Scanner(System.in);
        int totalProductos = 0;
        boolean entradaValida = false;

        do {
            try {
                System.out.print("\nIngrese el numero de productos: ");
                totalProductos = scanner.nextInt();

                if (totalProductos > 500) {
                    entradaValida = true;
                    System.out.println("Cantidad de productos ingresada correctamente: " + totalProductos);
                } else {
                    System.out.println("El numero de productos debe ser mayor a 500. Intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero entero.");
                scanner.next();
            }
        } while (!entradaValida);

        scanner.close();
    }
}
