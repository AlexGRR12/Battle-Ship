import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int TAMANO = 10;
        int DISPAROS_MAXIMOS = 32;
        int[] TAMANOS_BARCOS = {5, 4, 3, 3, 2};
        int[][] tablero = new int[TAMANO][TAMANO];
        boolean[][] disparos = new boolean[TAMANO][TAMANO];
        int[] impactos = new int[TAMANOS_BARCOS.length];

        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        int disparosRealizados = 0;
        int impactosTotales = 0;

        // Llenar el tablero con -1, que significa que no hay barcos ahí
        for (int i = 0; i < TAMANO; i++) {
            for (int j = 0; j < TAMANO; j++) {
                tablero[i][j] = -1;
            }
        }

        // Colocar barcos en posiciones aleatorias sin que se solapen
        for (int i = 0; i < TAMANOS_BARCOS.length; i++) {
            boolean colocado = false;
            while (!colocado) {
                int fila = random.nextInt(TAMANO);
                int columna = random.nextInt(TAMANO);
                boolean horizontal = random.nextBoolean();
                boolean valido = true;

                // Comprobar si el barco cabe en la posi sin chocar con otro
                for (int j = 0; j < TAMANOS_BARCOS[i]; j++) {
                    int f, c;
                    if (horizontal) {
                        f = fila;
                        c = columna + j;
                    } else {
                        f = fila + j;
                        c = columna;
                    }
                    if (f >= TAMANO || c >= TAMANO || tablero[f][c] != -1) {
                        valido = false;
                        break;
                    }
                }

                // Si la posición es valido, colocar el barco
                if (valido) {
                    for (int j = 0; j < TAMANOS_BARCOS[i]; j++) {
                        int f, c;
                        if (horizontal) {
                            f = fila;
                            c = columna + j;
                        } else {
                            f = fila + j;
                            c = columna;
                        }
                        tablero[f][c] = i;
                    }
                    colocado = true;
                }
            }
        }

        // Empieza la partida
        while (disparosRealizados < DISPAROS_MAXIMOS) {
            // Mostrar el tablero al jugador
            System.out.println("  1 2 3 4 5 6 7 8 9 10");
            for (int i = 0; i < TAMANO; i++) {
                System.out.print((char) ('A' + i) + " ");
                for (int j = 0; j < TAMANO; j++) {
                    if (!disparos[i][j]) {
                        System.out.print("· ");
                    } else if (tablero[i][j] == -1) {
                        System.out.print("~ ");
                    } else if (impactos[tablero[i][j]] == TAMANOS_BARCOS[tablero[i][j]]) {
                        System.out.print("# ");
                    } else {
                        System.out.print("* ");
                    }
                }
                System.out.println();
            }

            // Mostrar estadísticas de progreso
            System.out.println("\n--- Estadísticas ---");
            for (int i = 0; i < TAMANOS_BARCOS.length; i++) {
                System.out.println("Barco " + (i + 1) + " (" + TAMANOS_BARCOS[i] + " celdas): " +
                        impactos[i] + "/" + TAMANOS_BARCOS[i] + " impactos");
            }
            System.out.println("Impactos totales: " + impactosTotales);
            System.out.println("Disparos realizados: " + disparosRealizados);
            System.out.println("Disparos restantes: " + (DISPAROS_MAXIMOS - disparosRealizados));
            System.out.println("--------------------\n");

            // Pedir coordenadas al jugador
            System.out.print("Introduce coordenada (ej: A5): ");
            String entrada = scanner.next();
            if (entrada.length() < 2 || entrada.length() > 3) {
                continue;
            }

            // Obtener la fila (letra)
            String letras = "ABCDEFGHIJ";
            int fila = -1;
            for (int i = 0; i < letras.length(); i++) {
                if (entrada.startsWith(letras.substring(i, i + 1))) {
                    fila = i;
                    break;
                }
            }

            // Obtener la columna (número)
            int columna = 0;
            for (int i = 1; i < entrada.length(); i++) {
                char caracter = entrada.charAt(i);
                if (caracter >= '0' && caracter <= '9') {
                    columna = columna * 10 + (caracter - '0');
                }
            }
            columna--; // Ajustar porque el usuario pone del 1 al 10 y el array va de 0 a 9


            // Comprobar si la coordenada es válida
            if (fila < 0 || fila >= TAMANO || columna < 0 || columna >= TAMANO || disparos[fila][columna]) {
                System.out.println("Coordenada inválida o repetida.");
                continue;
            }

            // Registrar el disparo
            disparos[fila][columna] = true;
            disparosRealizados++;

            // Comprobar si ha acertado o fallado
            if (tablero[fila][columna] != -1) {
                impactos[tablero[fila][columna]]++;
                impactosTotales++;
                System.out.println("¡Impacto!");
            } else {
                System.out.println("Agua.");
            }

            // Comprobar si ha ganado
            if (impactosTotales == 17) {
                System.out.println("Has hundido todos los barcos, WIN");
                break;
            }
        }

        // Si se queda sin disparos, se acaba el juego y el jugador ha perdido
        if (impactosTotales < 17) {
            System.out.println("Sin disparos, LOSE");
        }
    }
}