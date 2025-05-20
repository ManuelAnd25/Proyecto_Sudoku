import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SudokuTest {

    private Sudoku sudoku;

    @BeforeEach
    public void setUp() {
        sudoku = new Sudoku();

        // Tablero vacío para iniciar
        int[][] tableroInicial = new int[9][9];
        boolean[][] celdasFijas = new boolean[9][9];

        // Por ejemplo, ponemos una celda fija en (0,0) con valor 5
        tableroInicial[0][0] = 5;
        celdasFijas[0][0] = true;

        sudoku.setTablero(tableroInicial, celdasFijas);
    }

    // --- Pruebas positivas ---

    @Test
    public void testMovimientoValidoCorrecto() throws SudokuException {
        // Colocar un número válido en una celda no fija y sin conflicto
        assertTrue(sudoku.colocarNumero(0, 1, 3));  // fila 0, columna 1, valor 3
        assertEquals(3, sudoku.getTablero()[0][1]);
    }

    // --- Pruebas negativas ---

    @Test
    public void testColocarNumeroFueraDeRango() {
        // Valor menor a 1
        assertThrows(OutOfRangeException.class, () -> sudoku.colocarNumero(0, 1, 0));
        // Valor mayor a 9
        assertThrows(OutOfRangeException.class, () -> sudoku.colocarNumero(0, 1, 10));
    }

    @Test
    public void testColocarNumeroEnCeldaFija() {
        // Intentar cambiar celda fija (0,0)
        assertThrows(InvalidMovementException.class, () -> sudoku.colocarNumero(0, 0, 4));
    }

    @Test
    public void testColocarNumeroEnPosicionConConflicto() throws SudokuException {
        // Colocar valor 5 en la fila 0, columna 1 (conflicto con la celda fija 5 en (0,0))
        assertThrows(InvalidMovementException.class, () -> sudoku.colocarNumero(0, 1, 5));
    }

    // --- Pruebas de borde ---

    @Test
    public void testColocarNumeroEnUltimaFilaYColumna() throws SudokuException {
        // Colocar un número válido en la última celda (8,8)
        assertTrue(sudoku.colocarNumero(8, 8, 7));
        assertEquals(7, sudoku.getTablero()[8][8]);
    }

    @Test
    public void testMovimientoValidoBordeTablero() {
        // Validar que no permite repetir número en la última columna
        try {
            sudoku.colocarNumero(0, 8, 5); // 5 ya está en fila 0 (en columna 0)
            fail("Debe lanzar excepción InvalidMovementException");
        } catch (InvalidMovementException e) {
            // Ok, se esperaba excepción
        } catch (SudokuException e) {
            fail("Se esperaba InvalidMovementException");
        }
    }

    // --- Prueba de estado resuelto ---

    @Test
    public void testEstaResueltoConTableroIncompleto() {
        assertFalse(sudoku.estaResuelto());
    }

    @Test
    public void testEstaResueltoConTableroCompletoYCorrecto() throws SudokuException {
        // Tablero resuelto válido de Sudoku (ejemplo clásico)
        int[][] solucion = {
                {5,3,4,6,7,8,9,1,2},
                {6,7,2,1,9,5,3,4,8},
                {1,9,8,3,4,2,5,6,7},
                {8,5,9,7,6,1,4,2,3},
                {4,2,6,8,5,3,7,9,1},
                {7,1,3,9,2,4,8,5,6},
                {9,6,1,5,3,7,2,8,4},
                {2,8,7,4,1,9,6,3,5},
                {3,4,5,2,8,6,1,7,9}
        };
        boolean[][] fijas = new boolean[9][9];  // Ninguna celda fija en este caso

        sudoku.setTablero(solucion, fijas);

        assertTrue(sudoku.estaResuelto());
    }
}