package Othello;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Objects;

import static Othello.ChipType.WHITE;


class StatusBar extends ImageView {

    private Image white = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("white100_pieceonly.png")).toExternalForm());
    private Image black = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("black100_pieceonly.png")).toExternalForm());
    private ImageView whiteImage = new ImageView(white);
    private ImageView blackImage = new ImageView(black);
    private Text whitescore = new Text();
    private Text blackscore = new Text();
    private Pane pane = new Pane();
    private double TRANSPARENT_STATUS = 0.3;
    private double NON_TRANSPARENT_STATUS = 0.9;
    private Board board;

    StatusBar(Board board) {
        this.board = board;
        pane.setPrefSize(80, 640);
        pane.setStyle("-fx-background-color: #caaac0");
        whiteImage.setY(160);
        blackImage.setY(480);
        whitescore.setX(20);
        whitescore.setY(130);
        blackscore.setX(20);
        blackscore.setY(450);
        Font font = new Font("TimesRoman", 40);
        whitescore.setFont(font);
        blackscore.setFont(font);
        pane.getChildren().addAll(whitescore, blackscore, whiteImage, blackImage);
    }

    Pane getStatusBar() {
        return pane;
    }

    void updateStatusBar() {
        whitescore.setText(board.getWhiteScore().toString());
        blackscore.setText(board.getBlackScore().toString());

        int STATUSBAR_FADE_TIME = 300;
        FadeTransition whiteStatusImageFade = new FadeTransition(Duration.millis(STATUSBAR_FADE_TIME), whiteImage);
        FadeTransition blackStatusImageFade = new FadeTransition(Duration.millis(STATUSBAR_FADE_TIME), blackImage);
        FadeTransition whiteStatusLabelFade = new FadeTransition(Duration.millis(STATUSBAR_FADE_TIME), whitescore);
        FadeTransition blackStatusLabelFade = new FadeTransition(Duration.millis(STATUSBAR_FADE_TIME), blackscore);

        if (board.getCurrentPlayer() == WHITE) {
            onFade(whiteStatusImageFade);
            onFade(whiteStatusLabelFade);

            offFade(blackStatusImageFade);
            offFade(blackStatusLabelFade);

        } else {
            onFade(blackStatusImageFade);
            onFade(blackStatusLabelFade);

            offFade(whiteStatusImageFade);
            offFade(whiteStatusLabelFade);
        }
    }

    private void onFade(FadeTransition object) {
        object.setFromValue(TRANSPARENT_STATUS);
        object.setToValue(NON_TRANSPARENT_STATUS);
        object.play();
    }

    private void offFade(FadeTransition object) {
        object.setFromValue(NON_TRANSPARENT_STATUS);
        object.setToValue(TRANSPARENT_STATUS);
        object.play();
    }
}