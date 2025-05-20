public class SudokuException extends Exception {
    public SudokuException(String mensaje) {
        super(mensaje);
    }

    public SudokuException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}