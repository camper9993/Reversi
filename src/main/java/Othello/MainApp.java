package Othello;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MainApp extends Application {

    private Button exit = new Button("Exit");
    private StatusBar status_bar = new StatusBar();
    private HBox hBox = new HBox();
    private Pane pane = new Pane();
    static Integer currentPlayer = 1;
    private Button restart = new Button("Restart");

    private static int[][] flipMassive = new int[8][8];
    private static int[][] board = new int[8][8];
    private Group squareGroup = new Group();
    private static Group chipGroup = new Group();
    final static double  SQUARE_SIZE = 80.0;
    private final int  HEIGHT = 8;
    private final int  WIDTH = 8;
    static Integer whiteChips = 4;
    static Integer blackChips = 4;
    private List<Pair<Integer,Integer>> directions;

    private void mouseSetChip (MouseEvent mouse) {
        int x = mouseXtoTileX(mouse.getSceneX()), y = mouseYtoTileY(mouse.getSceneY());
        setChip(x,y);
    }

    private int mouseXtoTileX(double x) {
        return (int) (x / SQUARE_SIZE);
    }

    private int mouseYtoTileY(double y) {
        return (int) (y / SQUARE_SIZE);
    }

    public Parent createBoard() {
        restart.setStyle("-fx-background-color: #FECC00");
        restart.setPrefSize(80,30);
        status_bar.getStatusBar().getChildren().add(restart);
        status_bar.updateStatusBar();
        buttonControl();
        pane.setPrefSize(640,640);
        for (int x = 0;x < 8; x++) {
            for (int y = 0;y < 8; y++) {
                Square square = new Square((x + y) % 2 == 0, x , y);
                squareGroup.getChildren().add(square);
            }
        }
        pane.getChildren().addAll(squareGroup);
        board[3][3] = 1;
        board[4][4] = 1;
        board[3][4] = 2;
        board[4][3] = 2;
        checkPossibleTurns();
        updateChips();
        pane.setOnMouseClicked(this::mouseSetChip);
        pane.getChildren().add(chipGroup);
        hBox.getChildren().addAll(pane,status_bar.getStatusBar());
        return hBox;
    }

    private void updateChips() {
        whiteChips = 0;
        blackChips = 0;
        chipGroup.getChildren().clear();
        for (int x = 0;x < WIDTH;x++) {
            for (int y = 0; y < HEIGHT;y++) {
                if (board[x][y] == 1)
                    whiteChips++;
                if (board[x][y] == 2)
                    blackChips++;
                Chip chip = new Chip(board[x][y],x,y);
                chipGroup.getChildren().add(chip);
            }
        }
    }

    private void changeTurn() {
        if (currentPlayer == 1)
            currentPlayer = 2;
        else currentPlayer = 1;
    }

    private boolean isOnBoard(int x, int y) {
        return (x < HEIGHT && x >= 0) && (y  < WIDTH && y >= 0);
    }

    private void checkPossibleTurns() {
        int opposingPlayer;
        if (currentPlayer == 1) {
            opposingPlayer = 2;
        }
        else opposingPlayer = 1;

        Integer x_check;
        Integer y_check;

        directions = new ArrayList<>();
        directions.add(new Pair<>(1,1));
        directions.add(new Pair<>(0,1));
        directions.add(new Pair<>(-1,1));
        directions.add(new Pair<>(-1,0));
        directions.add(new Pair<>(-1,-1));
        directions.add(new Pair<>(0,-1));
        directions.add(new Pair<>(1,-1));
        directions.add(new Pair<>(1,0));

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT;y++) {
                for (Pair<Integer,Integer> pair : directions) {
                    if (board[x][y] == currentPlayer && isOnBoard(x + pair.getKey(),y + pair.getValue())) {
                        x_check = x + pair.getKey();
                        y_check = y + pair.getValue();
                        if (isOnBoard(x_check,y_check) && board[x_check][y_check] == opposingPlayer) {
                            x_check += pair.getKey();
                            y_check += pair.getValue();
                            while (isOnBoard(x_check, y_check) && board[x_check][y_check] == opposingPlayer) {
                                x_check = x_check + pair.getKey();
                                y_check = y_check + pair.getValue();
                            }
                            if (isOnBoard(x_check, y_check) && board[x_check][y_check] == 0)
                                board[x_check][y_check] = 3;
                        }
                    }
                }
            }
        }
    }

    private void flipTheChip(int x,int y) {
        int opposingPlayer;
        if (currentPlayer == 1) {
            opposingPlayer = 2;
        }
        else opposingPlayer = 1;

        Integer x_check;
        Integer y_check;

        directions = new ArrayList<>();
        directions.add(new Pair<>(1,1));
        directions.add(new Pair<>(0,1));
        directions.add(new Pair<>(-1,1));
        directions.add(new Pair<>(-1,0));
        directions.add(new Pair<>(-1,-1));
        directions.add(new Pair<>(0,-1));
        directions.add(new Pair<>(1,-1));
        directions.add(new Pair<>(1,0));

        int i;

        for (Pair<Integer,Integer> pair : directions) {
            x_check = x + pair.getKey();
            y_check = y + pair.getValue();
            if (isOnBoard(x_check, y_check)) {
                if (board[x_check][y_check] == opposingPlayer) {
                    i = 0;
                    while (isOnBoard(x_check, y_check) && board[x_check][y_check] != currentPlayer) {
                        flipMassive[x_check][y_check] = currentPlayer;
                        x_check = x_check + pair.getKey();
                        y_check = y_check + pair.getValue();
                        i++;
                    }
                    if (!isOnBoard(x_check,y_check))
                        while (i != 0) {
                        flipMassive[x_check - pair.getKey() * i][y_check - pair.getValue() * i] = 0;
                        i--;
                        }
                    if (isOnBoard(x_check, y_check) && board[x_check][y_check] == currentPlayer) {
                        for (int a = 0; a < WIDTH; a++) {
                            for (int b = 0; b < HEIGHT; b++) {
                                if (flipMassive[a][b] != 0) {
                                    board[a][b] = flipMassive[a][b];
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void removeHelp() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (board[x][y] == 3)
                    board[x][y] = 0;
            }
        }
    }

    private void setChip(int x,int y) {
        if (board[x][y] == 3) {
            board[x][y] = currentPlayer;
            flipTheChip(x,y);
            changeTurn();
            removeHelp();
            checkPossibleTurns();
            updateChips();
            status_bar.updateStatusBar();
        }
        checkForGameOver();
    }

    private Boolean checkFullBoard() {
        for (int x = 0;x < WIDTH;x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (board[x][y] == 0 || board[x][y] == 3)
                    return false;
            }
        }
        return true;
    }

    private void checkForGameOver() {
        if(checkFullBoard()) {
            alertWindow();
            checkPossibleTurns();
            updateChips();
        }
        for (int x = 0;x < WIDTH;x++) {
            for (int y = 0;y < HEIGHT;y++) {
                if (board[x][y] == 3) {
                    return;
                }
            }
        }
        if (currentPlayer == 1) {
            currentPlayer = 2;
        }
        else currentPlayer = 1;
            status_bar.updateStatusBar();
            checkPossibleTurns();
            updateChips();
        for (int x = 0;x < WIDTH;x++) {
            for (int y = 0;y < HEIGHT;y++) {
                if (board[x][y] == 3) {
                    return;
                }
            }
        }
        alertWindow();
    }

    private void buttonControl() {
        restart.setOnAction(e -> refresh());
    }

    private void refresh() {
        board = new int[WIDTH][HEIGHT];
        flipMassive = new int[WIDTH][HEIGHT];
        currentPlayer = 1;
        chipGroup.getChildren().clear();
        board[3][3] = 1;
        board[4][4] = 1;
        board[3][4] = 2;
        board[4][3] = 2;
        checkPossibleTurns();
        updateChips();
        status_bar.updateStatusBar();
    }

    private void alertWindow () {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Play again?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            refresh();
        }
        else
            System.exit(0);
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createBoard());
        primaryStage.setTitle("Othello");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
