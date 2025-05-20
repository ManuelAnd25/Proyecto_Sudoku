import java.util.Random;

public class GeneradorSudoku {

    // Clase para devolver el tablero visible y la solución completa
    public static class TableroConSolucion {
        public int[][] tableroVisible;
        public int[][] solucionCompleta;

        public TableroConSolucion(int[][] visible, int[][] solucion) {
            this.tableroVisible = visible;
            this.solucionCompleta = solucion;
        }
    }

    // Nuevo método que devuelve tablero visible + solución, según dificultad
    public static TableroConSolucion generarTableroConSolucion(String dificultad) {
        int[][] solucion = generarTableroCompleto();
        int[][] visible = copiarMatriz(solucion);

        int vacias = switch (dificultad.toLowerCase()) {
            case "facil" -> 30;
            case "medio" -> 40;
            case "dificil" -> 50;
            default -> throw new IllegalArgumentException("Dificultad no válida");
        };

        eliminarCasillas(visible, vacias);
        return new TableroConSolucion(visible, solucion);
    }

    // Método original que solo devuelve el tablero con huecos (opcional si quieres mantenerlo)
    public static int[][] generarTablero(String dificultad) {
        int[][] tableroCompleto = generarTableroCompleto();
        int[][] tableroConHuecos = copiarMatriz(tableroCompleto);

        int casillasVacías;
        switch (dificultad.toLowerCase()) {
            case "facil" -> casillasVacías = 30;
            case "medio" -> casillasVacías = 40;
            case "dificil" -> casillasVacías = 50;
            default -> throw new IllegalArgumentException("Dificultad no válida");
        }

        eliminarCasillas(tableroConHuecos, casillasVacías);
        return tableroConHuecos;
    }

    public static boolean[][] generarCeldasFijas(int[][] tablero) {
        boolean[][] fijas = new boolean[9][9];
        for (int fila = 0; fila < 9; fila++) {
            for (int col = 0; col < 9; col++) {
                fijas[fila][col] = tablero[fila][col] != 0;
            }
        }
        return fijas;
    }

    private static int[][] generarTableroCompleto() {
        int[][] tablero = new int[9][9];
        llenar(tablero, 0, 0);
        return tablero;
    }

    private static boolean llenar(int[][] tablero, int fila, int col) {
        if (fila == 9) return true;
        if (col == 9) return llenar(tablero, fila + 1, 0);

        Random rand = new Random();
        int[] numeros = rand.ints(1, 10).distinct().limit(9).toArray();

        for (int num : numeros) {
            if (esValido(tablero, fila, col, num)) {
                tablero[fila][col] = num;
                if (llenar(tablero, fila, col + 1)) return true;
                tablero[fila][col] = 0;
            }
        }
        return false;
    }

    private static boolean esValido(int[][] tablero, int fila, int col, int val) {
        for (int i = 0; i < 9; i++) {
            if (tablero[fila][i] == val || tablero[i][col] == val) return false;
        }
        int startRow = fila - fila % 3;
        int startCol = col - col % 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (tablero[r][c] == val) return false;
            }
        }
        return true;
    }

    private static void eliminarCasillas(int[][] tablero, int cantidad) {
        Random rand = new Random();
        int eliminadas = 0;

        while (eliminadas < cantidad) {
            int fila = rand.nextInt(9);
            int col = rand.nextInt(9);
            if (tablero[fila][col] != 0) {
                tablero[fila][col] = 0;
                eliminadas++;
            }
        }
    }

    private static int[][] copiarMatriz(int[][] original) {
        int[][] copia = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(original[i], 0, copia[i], 0, 9);
        }
        return copia;
    }
}