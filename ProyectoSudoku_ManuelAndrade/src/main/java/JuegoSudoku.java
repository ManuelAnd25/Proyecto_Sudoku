import java.util.Scanner;

public class JuegoSudoku {
    private final Scanner scanner = new Scanner(System.in);
    private final Sudoku sudoku = new Sudoku();

    public void iniciar() {
        System.out.println("========== SUDOKU ==========");
        System.out.println("Selecciona la dificultad: fácil | medio | difícil");
        String dificultad = scanner.nextLine().toLowerCase();

        int[][] tableroInicial = GeneradorSudoku.generarTablero(dificultad);
        boolean[][] celdasFijas = GeneradorSudoku.generarCeldasFijas(tableroInicial);
        sudoku.setTablero(tableroInicial, celdasFijas);

        while (true) {
            mostrarTablero();
            if (sudoku.estaResuelto()) {
                System.out.println("🎉 ¡Felicidades! Has completado el Sudoku.");
                break;
            }

            System.out.println("Introduce tu jugada (fila columna valor) o escribe 'salir':");
            String entrada = scanner.nextLine();

            if (entrada.equalsIgnoreCase("salir")) {
                System.out.println("Juego terminado. ¡Hasta luego!");
                break;
            }

            try {
                String[] partes = entrada.trim().split("\\s+");
                if (partes.length != 3) throw new IllegalArgumentException("Formato inválido.");

                int fila = Integer.parseInt(partes[0]) - 1;
                int columna = Integer.parseInt(partes[1]) - 1;
                int valor = Integer.parseInt(partes[2]);

                sudoku.colocarNumero(fila, columna, valor);

            } catch (SudokuException e) {
                System.out.println("❌ " + e.getMessage());
            } catch (Exception e) {
                System.out.println("⚠️ Entrada no válida. Usa: fila columna valor (ej. 3 4 7)");
            }
        }
    }

    private void mostrarTablero() {
        int[][] tablero = sudoku.getTablero();
        System.out.println("\n   1 2 3   4 5 6   7 8 9");
        for (int fila = 0; fila < 9; fila++) {
            if (fila % 3 == 0) System.out.println("  +-------+-------+-------+");
            System.out.print((fila + 1) + " |");
            for (int col = 0; col < 9; col++) {
                int val = tablero[fila][col];
                System.out.print((val == 0 ? " " : val) + ((col + 1) % 3 == 0 ? " |" : " "));
            }
            System.out.println();
        }
        System.out.println("  +-------+-------+-------+\n");
    }

    public static void main(String[] args) {
        JuegoSudoku juego = new JuegoSudoku();
        juego.iniciar();
    }
}