public class Sudoku {
    private int[][] tablero;
    private boolean[][] celdasFijas;

    public Sudoku() {
        tablero = new int[9][9];
        celdasFijas = new boolean[9][9];
    }

    public void setTablero(int[][] nuevoTablero, boolean[][] nuevasCeldasFijas) {
        tablero = new int[9][9];
        celdasFijas = new boolean[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(nuevoTablero[i], 0, tablero[i], 0, 9);
            System.arraycopy(nuevasCeldasFijas[i], 0, celdasFijas[i], 0, 9);
        }
    }

    public boolean esMovimientoValido(int fila, int columna, int valor) {
        if (valor < 1 || valor > 9) return false;

        // Verificar fila (ignorar la celda actual)
        for (int i = 0; i < 9; i++) {
            if (i != columna && tablero[fila][i] == valor) return false;
        }

        // Verificar columna (ignorar la celda actual)
        for (int i = 0; i < 9; i++) {
            if (i != fila && tablero[i][columna] == valor) return false;
        }

        // Verificar caja 3x3 (ignorar la celda actual)
        int startRow = fila - fila % 3;
        int startCol = columna - columna % 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if ((r != fila || c != columna) && tablero[r][c] == valor) return false;
            }
        }

        return true;
    }

    public boolean colocarNumero(int fila, int columna, int valor) throws SudokuException {
        if (valor < 1 || valor > 9) {
            throw new OutOfRangeException(valor);
        }

        if (celdasFijas[fila][columna]) {
            throw new InvalidMovementException(fila + 1, columna + 1, valor);
        }

        if (!esMovimientoValido(fila, columna, valor)) {
            throw new InvalidMovementException(fila + 1, columna + 1, valor);
        }

        tablero[fila][columna] = valor;
        return true;
    }

    public boolean estaResuelto() {
        for (int fila = 0; fila < 9; fila++) {
            for (int columna = 0; columna < 9; columna++) {
                int valor = tablero[fila][columna];
                if (valor == 0) {
                    System.out.println("Casilla vacía en fila " + fila + ", columna " + columna);
                    return false;
                }

                // Guardar valor temporalmente
                tablero[fila][columna] = 0;

                if (!esMovimientoValido(fila, columna, valor)) {
                    System.out.println("Conflicto con valor " + valor + " en fila " + fila + ", columna " + columna);
                    tablero[fila][columna] = valor;
                    return false;
                }

                // Restaurar valor
                tablero[fila][columna] = valor;
            }
        }
        return true;
    }

    public int[][] getTablero() {
        return tablero;
    }

    public boolean[][] getCeldasFijas() {
        return celdasFijas;
    }
}