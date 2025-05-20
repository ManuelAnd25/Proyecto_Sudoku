public class InvalidMovementException extends SudokuException {
    public InvalidMovementException(int fila, int columna, int valor) {
        super("Movimiento inválido: No se puede colocar el número " + valor +
                " en la posición [" + fila + "," + columna + "]. " +
                "El número viola las reglas del Sudoku.");
    }
}