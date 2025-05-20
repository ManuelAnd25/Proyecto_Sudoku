public class OutOfRangeException extends SudokuException {
    public OutOfRangeException(int valor) {
        super("Entrada fuera de rango: " + valor +
                ". Debe ser un número entre 1 y 9.");
    }
}