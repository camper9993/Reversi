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
import java.util.Objects;
import java.util.Optional;
import static Othello.ChipType.*;


public class MainApp extends Application {

    private String winner = "";
    private StatusBar status_bar = new StatusBar();
    private HBox hBox = new HBox();
    private Pane pane = new Pane();
    static ChipType currentPlayer = WHITE;
    private Button restart = new Button("Restart");

    private final int  HEIGHT = 8;
    private final int  WIDTH = 8;
    private ChipType[][] flipMassive = new ChipType[WIDTH][HEIGHT];
    private ChipType[][] board = new ChipType[WIDTH][HEIGHT];
    private Group squareGroup = new Group();
    private Group chipGroup = new Group();
    final static double  SQUARE_SIZE = 80.0;
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

    private void addDirections() {
        directions = new ArrayList<>();
        directions.add(new Pair<>(1,1));
        directions.add(new Pair<>(0,1));
        directions.add(new Pair<>(-1,1));
        directions.add(new Pair<>(-1,0));
        directions.add(new Pair<>(-1,-1));
        directions.add(new Pair<>(0,-1));
        directions.add(new Pair<>(1,-1));
        directions.add(new Pair<>(1,0));
    }

    private Parent createBoard() {
        restart.setStyle("-fx-background-color: #FECC00");
        restart.setPrefSize(80,30);
        status_bar.getStatusBar().getChildren().add(restart);
        status_bar.updateStatusBar();
        buttonControl();
        pane.setPrefSize(640,640);
        for (int x = 0;x < WIDTH; x++) {
            for (int y = 0;y < HEIGHT; y++) {
                Square square = new Square((x + y) % 2 == 0, x , y);
                squareGroup.getChildren().add(square);
                flipMassive[x][y] = EMPTY;
                board[x][y] = EMPTY;
            }
        }
        addDirections();
        pane.getChildren().addAll(squareGroup);
        board[3][3] = WHITE;
        board[4][4] = board[3][3];
        board[3][4] = BLACK;
        board[4][3] = board[3][4];
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
                if (board[x][y] == WHITE)
                    whiteChips++;
                if (board[x][y] == BLACK)
                    blackChips++;
                Chip chip = new Chip(board[x][y],x,y);
                chipGroup.getChildren().add(chip);
            }
        }
    }

    private void changeTurn() {
        if (currentPlayer == WHITE)
            currentPlayer = BLACK;
        else currentPlayer = WHITE;
    }

    private boolean isOnBoard(int x, int y) {
        return (x < HEIGHT && x >= 0) && (y  < WIDTH && y >= 0);
    }

    private void checkPossibleTurns() {
        ChipType opposingPlayer;
        if (currentPlayer == WHITE) {
            opposingPlayer = BLACK;
        }
        else opposingPlayer = WHITE;

        Integer x_check;
        Integer y_check;

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
                            if (isOnBoard(x_check, y_check) && board[x_check][y_check] == EMPTY)
                                board[x_check][y_check] = HELP;
                        }
                    }
                }
            }
        }
    }

    private void flipTheChip(int x,int y) {
        ChipType opposingPlayer;
        if (currentPlayer == WHITE) {
            opposingPlayer = BLACK;
        }
        else opposingPlayer = WHITE;

        int x_check;
        int y_check;

        int i;

        for (Pair<Integer,Integer> pair : directions) {
            x_check = x + pair.getKey();
            y_check = y + pair.getValue();
            i = 0;
            if (isOnBoard(x_check, y_check)) {
                if (board[x_check][y_check] == opposingPlayer) {
                    while (isOnBoard(x_check, y_check) && board[x_check][y_check] == opposingPlayer) {
                        flipMassive[x_check][y_check] = currentPlayer;
                        x_check = x_check + pair.getKey();
                        y_check = y_check + pair.getValue();
                        i++;
                    }
                    if (!isOnBoard(x_check,y_check) || board[x_check][y_check] != currentPlayer)
                        while (i != 0) {
                        flipMassive[x_check - pair.getKey() * i][y_check - pair.getValue() * i] = EMPTY;
                        i--;
                        }
                    else {
                        for (int a = 0; a < WIDTH; a++) {
                            for (int b = 0; b < HEIGHT; b++) {
                                if (flipMassive[a][b] != EMPTY) {
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
                if (board[x][y] == HELP)
                    board[x][y] = EMPTY;
            }
        }
    }

    private void setChip(int x,int y) {
        if (board[x][y] == HELP) {
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
                if (board[x][y] == EMPTY|| board[x][y] == HELP)
                    return false;
            }
        }
        return true;
    }

    private void checkForGameOver() {
        if(checkFullBoard()) {
            if (blackChips > whiteChips) {
                winner = "Black win's!";
            }
            if (whiteChips > blackChips)
                winner = "White win's!";
            if (Objects.equals(blackChips, whiteChips))
                winner = "It's a tie!";
            alertWindow();
            checkPossibleTurns();
            updateChips();
        }
        for (int x = 0;x < WIDTH;x++) {
            for (int y = 0;y < HEIGHT;y++) {
                if (board[x][y] == HELP) {
                    return;
                }
            }
        }
        changeTurnAlert();
        if (currentPlayer == WHITE) {
            currentPlayer = BLACK;
        }
        else currentPlayer = WHITE;
            status_bar.updateStatusBar();
            checkPossibleTurns();
            updateChips();
        for (int x = 0;x < WIDTH;x++) {
            for (int y = 0;y < HEIGHT;y++) {
                if (board[x][y] == HELP) {
                    return;
                }
            }
        }
        if (blackChips > whiteChips) {
            winner = "Black win's!";
        }
        if (whiteChips > blackChips)
            winner = "White win's!";
        if (Objects.equals(blackChips, whiteChips))
            winner = "It's a tie!";
        alertWindow();
    }

    private void buttonControl() {
        restart.setOnAction(e -> refresh());
    }

    private void refresh() {
        board = new ChipType[WIDTH][HEIGHT];
        flipMassive = new ChipType[WIDTH][HEIGHT];
        currentPlayer = WHITE;
        for (int x = 0;x < WIDTH;x++)
            for (int y = 0;y < HEIGHT;y++) {
            flipMassive[x][y] = EMPTY;
            board[x][y] = EMPTY;
            }
        chipGroup.getChildren().clear();
        board[3][3] = WHITE;
        board[4][4] = board[3][3];
        board[3][4] = BLACK;
        board[4][3] = board[3][4];
        checkPossibleTurns();
        updateChips();
        status_bar.updateStatusBar();
    }

    private void alertWindow () {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(winner + "\n" + "Play again?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            refresh();
        }
        else
            System.exit(0);
    }

    private void changeTurnAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle
                ("Change turn");
        alert.setHeaderText("Player skip's turn!");
        alert.showAndWait();
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
