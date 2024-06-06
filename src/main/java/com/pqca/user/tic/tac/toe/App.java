package com.pqca.user.tic.tac.toe;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

    private char currentPlayer = 'X';
    private Button[][] buttons = new Button[3][3];

    @Override
    public void start(Stage primaryStage) {
        // Load the Ink Free font
        Font.loadFont(getClass().getResourceAsStream("/fonts/Inkfree.ttf"), 12);

        BorderPane borderPane = new BorderPane();

        Label titleLabel = new Label("Tic Tac Toe");
        titleLabel.setStyle("-fx-font-size: 36; -fx-font-weight: bold; -fx-font-family: 'Ink Free';");
        borderPane.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: #d9d9d9;");
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // General styling for all buttons
        String generalStyle = "-fx-font-size: 24; -fx-alignment: center;";

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setPrefSize(100, 100);

                // Set general style
                button.setStyle(generalStyle);

                button.setOnAction(event -> {
                    if (button.getText().isEmpty()) {
                        button.setText(currentPlayer + "");
                        if (isWinner()) {
                            showAlert(currentPlayer + " wins!");
                            primaryStage.close();
                        } else if (isBoardFull()) {
                            showAlert("It's a draw!");
                            primaryStage.close();
                        } else {
                            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                            updateButtonStyles();
                        }
                    }
                });

                buttons[i][j] = button;
                gridPane.add(button, j, i);
            }
        }

        borderPane.setCenter(gridPane);
        BorderPane.setAlignment(gridPane, Pos.CENTER);

        Scene scene = new Scene(borderPane, 320, 380);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.show();
    }

    private boolean isWinner() {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(currentPlayer + "")
                    && buttons[i][1].getText().equals(currentPlayer + "")
                    && buttons[i][2].getText().equals(currentPlayer + ""))
                return true;
            if (buttons[0][i].getText().equals(currentPlayer + "")
                    && buttons[1][i].getText().equals(currentPlayer + "")
                    && buttons[2][i].getText().equals(currentPlayer + ""))
                return true;
        }
        if (buttons[0][0].getText().equals(currentPlayer + "")
                && buttons[1][1].getText().equals(currentPlayer + "")
                && buttons[2][2].getText().equals(currentPlayer + ""))
            return true;
        if (buttons[0][2].getText().equals(currentPlayer + "")
                && buttons[1][1].getText().equals(currentPlayer + "")
                && buttons[2][0].getText().equals(currentPlayer + ""))
            return true;
        return false;
    }

    private boolean isBoardFull() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                if (button.getText().isEmpty())
                    return false;
            }
        }
        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateButtonStyles() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                if (!button.getText().isEmpty()) {
                    if (button.getText().charAt(0) == 'X') {
                        button.setTextFill(Color.RED);
                    } else {
                        button.setTextFill(Color.BLUE);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
