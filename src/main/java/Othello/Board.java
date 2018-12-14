package Othello;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import static Othello.ChipType.*;
import static Othello.Menu.botwork;


public class Board {

    private List<Point> possibleMoves;
    private StackPane root = new StackPane();
    private Menu menu = new Menu();
    private String winner = "";
    private StatusBar status_bar = new StatusBar(this);
    private HBox hBox = new HBox();
    private Pane pane = new Pane();
    private ChipType currentPlayer = WHITE;
    private Button restart = new Button("Restart");
    private final static int  HEIGHT = 8;
    final static int  WIDTH = 8;
    private ChipType[][] flipMassive = new ChipType[WIDTH][HEIGHT];
    private ChipType[][] board = new ChipType[WIDTH][HEIGHT];
    private Group squareGroup = new Group();
    private Group chipGroup = new Group();
    final static double  SQUARE_SIZE = 80.0;
    private Integer whiteChips = 4;
    private Integer blackChips = 4;
    private List<Pair<Integer,Integer>> directions;
    private Bot bot;
    private CornerPoint cornerPoint;
    private Point cornerMove;


    private void mouseSetChip (MouseEvent mouse) {
        int x = mouseXtoTileX(mouse.getSceneX()), y = mouseYtoTileY(mouse.getSceneY());
        setChipUsual(x,y);
        Point point = cornerMove;
        if (botwork && currentPlayer == BLACK) {
            if (possibleMoves.contains(point)) {
                setChip(point.x,point.y);
                checkForGameOverBot();
            }
            else {
                point = bot.nextTurn(this);
                setChip(point.x, point.y);
                checkForGameOverBot();
            }
        }
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

    private void drawBoard(){
        for (int x = 0;x < WIDTH; x++) {
            for (int y = 0;y < HEIGHT; y++) {
                Square square = new Square((x + y) % 2 == 0, x , y);
                squareGroup.getChildren().add(square);
                flipMassive[x][y] = EMPTY;
                board[x][y] = EMPTY;
            }
        }
    }

    Parent createBoard() {
        root.setPrefSize(720,640);
        restart.setStyle("-fx-background-color: #FECC00");
        restart.setPrefSize(80,30);
        status_bar.getStatusBar().getChildren().add(restart);
        status_bar.updateStatusBar();
        buttonControl();
        pane.setPrefSize(640,640);
        drawBoard();
        addDirections();
        bot = new Bot();
        pane.getChildren().addAll(squareGroup);
        board[3][3] = WHITE;
        board[4][4] = board[3][3];
        board[3][4] = BLACK;
        board[4][3] = board[3][4];
        checkPossibleTurns(currentPlayer);
        updateChips();
        pane.setOnMouseClicked(this::mouseSetChip);
        pane.getChildren().add(chipGroup);
        hBox.getChildren().addAll(pane,status_bar.getStatusBar());
        root.getChildren().addAll(hBox,menu.getMenu());
        root.setOnKeyPressed(k -> {
            KeyCode code =k.getCode();
            if (code.equals(KeyCode.ESCAPE))
                menu.getMenu().toFront();
        });
        return root;
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

    private void updateScore() {
        whiteChips = 0;
        blackChips = 0;
        for (int x = 0;x < WIDTH;x++) {
            for (int y = 0; y < HEIGHT;y++) {
                if (board[x][y] == WHITE)
                    whiteChips++;
                if (board[x][y] == BLACK)
                    blackChips++;
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

    void checkPossibleTurns(ChipType player) {
        possibleMoves = new ArrayList<>();
        ChipType opposingPlayer;
        opposingPlayer = player == WHITE ? BLACK : WHITE;
        Integer x_check;
        Integer y_check;

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT;y++) {
                for (Pair<Integer,Integer> pair : directions) {
                    if (board[x][y] == player && isOnBoard(x + pair.getKey(),y + pair.getValue())) {
                        x_check = x + pair.getKey();
                        y_check = y + pair.getValue();
                        if (isOnBoard(x_check,y_check) && board[x_check][y_check] == opposingPlayer) {
                            x_check += pair.getKey();
                            y_check += pair.getValue();
                            while (isOnBoard(x_check, y_check) && board[x_check][y_check] == opposingPlayer) {
                                x_check = x_check + pair.getKey();
                                y_check = y_check + pair.getValue();
                            }
                            if (isOnBoard(x_check, y_check) && board[x_check][y_check] == EMPTY) {
                                board[x_check][y_check] = HELP;
                                Point point = new Point(x_check,y_check);
                                possibleMoves.add(point);
                                CornerPoint cornerPoint = new CornerPoint(point);
                                if (cornerPoint.isCornerPoint())
                                    cornerMove = point;
                            }
                        }
                    }
                }
            }
        }
    }

    private void flipTheChip(int x, int y) {
        ChipType opposingPlayer = getOpponent();

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

    void removeHelp() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (board[x][y] == HELP)
                    board[x][y] = EMPTY;
            }
        }
    }

    private void setChipBot(int x, int y) {
        setChip(x, y);
    }

    private void setChipUsual(int x, int y) {
        if (board[x][y] == HELP) {
            setChip(x, y);
        }
        checkForGameOver();
    }

    private void setChip(int x, int y) {
        board[x][y] = currentPlayer;
        flipTheChip(x,y);
        changeTurn();
        removeHelp();
        checkPossibleTurns(currentPlayer);
        updateChips();
        status_bar.updateStatusBar();
    }

    void updateBoard(int x, int y) {
            board[x][y] = currentPlayer;
            flipTheChip(x,y);
            updateScore();
            changeTurn();
            removeHelp();
            checkPossibleTurns(currentPlayer);
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

    private void checkForGameOverBot() {
        fullBoard();
        for (int x = 0;x < WIDTH;x++) {
            for (int y = 0;y < HEIGHT;y++) {
                if (board[x][y] == HELP) {
                    return;
                }
            }
        }
        changeTurnAlert();
        changeCurrentPlayer();
        status_bar.updateStatusBar();
        checkPossibleTurns(currentPlayer);
        updateChips();
        for (int x = 0;x < WIDTH;x++) {
            for (int y = 0;y < HEIGHT;y++) {
                if (board[x][y] == HELP) {
                    Point point = bot.nextTurn(this);
                    setChipBot(point.x,point.y);
                    checkForGameOverBot();
                    return;
                }
            }
        }
        setWinner();
        alertWindow();
    }

    private void fullBoard() {
        if(checkFullBoard()) {
            setWinner();
            alertWindow();
            checkPossibleTurns(currentPlayer);
            updateChips();
        }
    }

    private void checkForGameOver() {
        fullBoard();
        if (!possibleMoves.isEmpty())
            return;
        changeTurnAlert();
        if (currentPlayer == WHITE) {
            currentPlayer = BLACK;
        }
        else currentPlayer = WHITE;
            status_bar.updateStatusBar();
            checkPossibleTurns(currentPlayer);
            updateChips();
        if (!possibleMoves.isEmpty())
            return;
        setWinner();
        alertWindow();
    }
    private void setWinner() {
        if (blackChips > whiteChips) {
            winner = "Black win's!";
        }
        if (whiteChips > blackChips)
            winner = "White win's!";
        if (Objects.equals(blackChips, whiteChips))
            winner = "It's a tie!";
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
        checkPossibleTurns(currentPlayer);
        updateChips();
        bot = new Bot();
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

    public ChipType[][] getCurrentState() {
        ChipType[][] currentState = new ChipType[WIDTH][HEIGHT];
        for (int i = 0;i < WIDTH;i++)
            System.arraycopy(board[i], 0, currentState[i], 0, HEIGHT);
        return currentState;
    }
    ChipType getCurrentPlayer() {
        return currentPlayer;
    }

    Integer getBlackScore() {
        return blackChips;
    }

    Integer getWhiteScore() {
        return whiteChips;
    }

    private ChipType changeCurrentPlayer() {
        if (currentPlayer == WHITE)
            currentPlayer = BLACK;
        else currentPlayer = WHITE;
        return currentPlayer;
    }

    ChipType getOpponent(){
        if (currentPlayer == WHITE)
            return BLACK;
        else
            return WHITE;
    }

    List<Point> getPossibleMoves() {
        return possibleMoves;
    }

    Board cloneBoard() {
        Board board = new Board();
        for (int x = 0;x < WIDTH;x++) {
            for (int y = 0;y < WIDTH;y++) {
                board.board[x][y] = this.board[x][y];
                board.flipMassive[x][y] = this.flipMassive[x][y];
            }
            board.possibleMoves = this.possibleMoves;
            board.currentPlayer = this.currentPlayer;
            board.directions = this.directions;
            board.blackChips = this.blackChips;
            board.whiteChips = this.whiteChips;
        }
        return board;
    }

    public int getCurrentScore() {
        return blackChips - whiteChips;
    }
}
