import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SudokuGUI extends Application {

    private Sudoku sudoku = new Sudoku();
    private TextField[][] campos = new TextField[9][9];
    private int[][] solucion;

    @Override
    public void start(Stage primaryStage) {
        String dificultad = pedirDificultad();
        GeneradorSudoku.TableroConSolucion data = GeneradorSudoku.generarTableroConSolucion(dificultad);
        int[][] tableroVisible = data.tableroVisible;
        solucion = data.solucionCompleta;

        boolean[][] fijas = GeneradorSudoku.generarCeldasFijas(tableroVisible);
        sudoku.setTablero(tableroVisible, fijas);
        final boolean[][] fijasFinal = fijas;

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(2);
        grid.setHgap(2);
        grid.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;");

        for (int fila = 0; fila < 9; fila++) {
            for (int col = 0; col < 9; col++) {
                TextField campo = new TextField();
                campo.setPrefSize(50, 50);
                campo.setAlignment(Pos.CENTER);
                int valor = tableroVisible[fila][col];

                if (valor != 0) {
                    campo.setText(String.valueOf(valor));
                    campo.setDisable(true);
                    campo.setStyle("-fx-background-color: #dcdcdc;");
                }

                final int f = fila;
                final int c = col;

                campo.setOnAction(e -> {
                    try {
                        int input = Integer.parseInt(campo.getText());
                        sudoku.colocarNumero(f, c, input);

                        if (input != solucion[f][c]) {
                            campo.setStyle("-fx-background-color: #ffb3b3;");
                        } else {
                            campo.setStyle("-fx-background-color: #ffffff;");
                        }

                        if (sudoku.estaResuelto()) {
                            mostrarAlerta("🎉 ¡Felicidades!", "Has completado el Sudoku correctamente.");
                        }

                    } catch (NumberFormatException ex) {
                        campo.setText("");
                    } catch (SudokuException ex) {
                        campo.setStyle("-fx-background-color: #ffb3b3;");
                        mostrarAlerta("❌ Error", ex.getMessage());
                    }
                });

                campos[fila][col] = campo;
                grid.add(campo, col, fila);
            }
        }

        Button btnVerificar = new Button("Verificar");
        btnVerificar.setOnAction(e -> {
            boolean todoCorrecto = true;
            boolean hayCampoVacio = false;
            boolean hayValorFueraDeRango = false;
            boolean hayConflictoSudoku = false;

            for (int fila = 0; fila < 9; fila++) {
                for (int col = 0; col < 9; col++) {
                    if (!fijasFinal[fila][col]) {
                        TextField campo = campos[fila][col];
                        String texto = campo.getText();
                        int valor;

                        try {
                            valor = Integer.parseInt(texto);
                            sudoku.colocarNumero(fila, col, valor);

                            if (valor != solucion[fila][col]) {
                                campo.setStyle("-fx-background-color: #ffb3b3;");
                                todoCorrecto = false;
                            } else {
                                campo.setStyle("-fx-background-color: #ffffff;");
                            }

                        } catch (NumberFormatException ex) {
                            campo.setStyle("-fx-background-color: #ffb3b3;");
                            hayCampoVacio = true;
                            todoCorrecto = false;
                        } catch (OutOfRangeException ex) {
                            campo.setStyle("-fx-background-color: #ffb3b3;");
                            hayValorFueraDeRango = true;
                            todoCorrecto = false;
                        } catch (SudokuException ex) {
                            campo.setStyle("-fx-background-color: #ffb3b3;");
                            hayConflictoSudoku = true;
                            todoCorrecto = false;
                        }
                    }
                }
            }

            if (hayCampoVacio) {
                mostrarAlerta("❌ Error", "Por favor, completa todas las casillas con números válidos.");
            } else if (hayValorFueraDeRango) {
                mostrarAlerta("❌ Error", "Por favor, introduce valores entre 1 y 9.");
            } else if (hayConflictoSudoku) {
                mostrarAlerta("❌ Error", "Hay conflictos con las reglas del Sudoku.");
            } else if (todoCorrecto) {
                mostrarAlerta("🎉 ¡Felicidades!", "Has completado el Sudoku correctamente.");
            } else {
                mostrarAlerta("❌ Sudoku incompleto o incorrecto", "Sigue intentando.");
            }
        });

        VBox root = new VBox(15, grid, btnVerificar);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Sudoku - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private String pedirDificultad() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("medio", "facil", "medio", "dificil");
        dialog.setTitle("Dificultad");
        dialog.setHeaderText("Selecciona el nivel de dificultad:");
        dialog.setContentText("Nivel:");
        return dialog.showAndWait().orElse("medio");
    }

    public static void main(String[] args) {
        launch();
    }
}