package Othello;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.text.Font;

import static Othello.MainApp.*;


public class StatusBar extends ImageView {

    final double NON_TRANSPARENT_STATUS = 0.9;
    final double TRANSPARENT_STATUS = 0.3;
    final int STATUSBAR_FADE_TIME = 250;
    private Image white = new Image(getClass().getClassLoader().getResource("white100_pieceonly.png").toExternalForm());
    private Image black = new Image(getClass().getClassLoader().getResource("black100_pieceonly.png").toExternalForm());
    private ImageView rightImage = new ImageView(white);
    private ImageView leftImage = new ImageView(black);
    private Text whitescore = new Text();
    private Text blackscore = new Text();
    private Pane pane = new Pane();
    private Font font = new Font("TimesRoman",40);

    StatusBar () {
        pane.setPrefSize(80,640);
        pane.setStyle("-fx-background-color: #0c342a");
        rightImage.setY(160);
        leftImage.setY(480);
        whitescore.setX(20);
        whitescore.setY(130);
        blackscore.setX(20);
        blackscore.setY(450);
        whitescore.setFont(font);
        blackscore.setFont(font);
        pane.getChildren().addAll(whitescore,blackscore,rightImage,leftImage);
    }

    public Pane getStatusBar() {
        return pane;
    }

    public void updateStatusBar() {
        whitescore.setText(whiteChips.toString());
        blackscore.setText(blackChips.toString());

        FadeTransition rightStatusImageFade = new FadeTransition(Duration.millis(STATUSBAR_FADE_TIME), rightImage);
        FadeTransition leftStatusImageFade = new FadeTransition(Duration.millis(STATUSBAR_FADE_TIME), leftImage);
        FadeTransition rightStatusLabelFade = new FadeTransition(Duration.millis(STATUSBAR_FADE_TIME), whitescore);
        FadeTransition leftStatusLabelFade = new FadeTransition(Duration.millis(STATUSBAR_FADE_TIME), blackscore);

        if (currentPlayer == 1) {
            rightStatusImageFade.setFromValue(TRANSPARENT_STATUS);
            rightStatusImageFade.setToValue(NON_TRANSPARENT_STATUS);
            rightStatusImageFade.play();
            rightStatusLabelFade.setFromValue(TRANSPARENT_STATUS);
            rightStatusLabelFade.setToValue(NON_TRANSPARENT_STATUS);
            rightStatusLabelFade.play();


            leftStatusImageFade.setFromValue(NON_TRANSPARENT_STATUS);
            leftStatusImageFade.setToValue(TRANSPARENT_STATUS);
            leftStatusImageFade.play();
            leftStatusLabelFade.setFromValue(NON_TRANSPARENT_STATUS);
            leftStatusLabelFade.setToValue(TRANSPARENT_STATUS);
            leftStatusLabelFade.play();

        } else  {

            rightStatusImageFade.setFromValue(NON_TRANSPARENT_STATUS);
            rightStatusImageFade.setToValue(TRANSPARENT_STATUS);
            rightStatusImageFade.play();
            rightStatusLabelFade.setFromValue(NON_TRANSPARENT_STATUS);
            rightStatusLabelFade.setToValue(TRANSPARENT_STATUS);
            rightStatusLabelFade.play();

            leftStatusImageFade.setFromValue(TRANSPARENT_STATUS);
            leftStatusImageFade.setToValue(NON_TRANSPARENT_STATUS);
            leftStatusImageFade.play();
            leftStatusLabelFade.setFromValue(TRANSPARENT_STATUS);
            leftStatusLabelFade.setToValue(NON_TRANSPARENT_STATUS);
            leftStatusLabelFade.play();
        }
    }

}
