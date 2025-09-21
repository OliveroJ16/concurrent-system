import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        final int capacidadCadena = 500;
        int cantidadProductos = 0;
        boolean entradaValida = false;

        // Solicitar número de productos
        do {
            try {
                System.out.print("\nIngrese el numero de productos: ");
                cantidadProductos = scanner.nextInt();

                if (cantidadProductos > 500) {
                    entradaValida = true;
                    System.out.println("Cantidad de productos ingresada correctamente: " + cantidadProductos);
                } else {
                    System.out.println("El numero de productos debe ser mayor a 500. Intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero entero.");
                scanner.next();
            }
        } while (!entradaValida);
        scanner.close();

        // Crear la cinta de montaje
        CadenaMontaje cadenaMontaje = new CadenaMontaje(capacidadCadena, cantidadProductos);

        // Crear contenedores por tipo de producto (thread-safe)
        List<Producto> contenedor1 = Collections.synchronizedList(new ArrayList<>());
        List<Producto> contenedor2 = Collections.synchronizedList(new ArrayList<>());
        List<Producto> contenedor3 = Collections.synchronizedList(new ArrayList<>());

        // Crear colocadores: 2 por tipo
        Thread[] colocadores = new Thread[6];
        for (int i = 0; i < 6; i++) {
            int tipo = (i / 2) + 1;
            colocadores[i] = new Colocador(tipo, cadenaMontaje);
        }

        // Crear empaquetadores: 2 por tipo
        Thread[] empaquetadores = new Thread[6];
        for (int i = 0; i < 6; i++) {
            int tipo = (i / 2) + 1;
            List<Producto> contenedor;
            switch (tipo) {
                case 1 -> contenedor = contenedor1;
                case 2 -> contenedor = contenedor2;
                default -> contenedor = contenedor3;
            }
            empaquetadores[i] = new Empaquetador(tipo, cadenaMontaje, contenedor);
        }

        // Iniciar todos los colocadores
        for (Thread t : colocadores) {
            t.start();
        }

        // Iniciar todos los empaquetadores
        for (Thread t : empaquetadores) {
            t.start();
        }

        // Esperar a que los colocadores terminen
        for (Thread t : colocadores) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Avisar a los empaquetadores que los colocadores han terminado
        for (Thread t : empaquetadores) {
            ((Empaquetador) t).setColocadoresTerminaron(true);
        }

        // Esperar a que los empaquetadores terminen
        for (Thread t : empaquetadores) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Log final
        int totalEmpaquetado = contenedor1.size() + contenedor2.size() + contenedor3.size();
        
        System.out.println("\n=== Resumen final ===");
        System.out.println("Total productos colocados: " + cadenaMontaje.getTotalAcomodados());
        System.out.println("Cantidad total de productos esperados: " + cantidadProductos);
        System.out.println("Contenedor tipo 1: " + contenedor1.size());
        System.out.println("Contenedor tipo 2: " + contenedor2.size());
        System.out.println("Contenedor tipo 3: " + contenedor3.size());
        System.out.println("Total productos empaquetados: " + totalEmpaquetado);
        
        // Verificación
        if (cadenaMontaje.getTotalAcomodados() == cantidadProductos) {
            System.out.println("✓ Cantidad de productos colocados CORRECTA");
        } else {
            System.out.println("✗ ERROR: Se colocaron " + cadenaMontaje.getTotalAcomodados() + 
                             " productos pero se esperaban " + cantidadProductos);
        }
        
        if (totalEmpaquetado == cantidadProductos) {
            System.out.println("✓ Cantidad de productos empaquetados CORRECTA");
        } else {
            System.out.println("✗ ERROR: Se empaquetaron " + totalEmpaquetado + 
                             " productos pero se esperaban " + cantidadProductos);
        }
    }
}