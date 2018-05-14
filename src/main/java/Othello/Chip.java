package Othello;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Chip extends ImageView {
    private Image white = new Image(getClass().getClassLoader().getResource("white100_pieceonly.png").toExternalForm());
    private Image black = new Image(getClass().getClassLoader().getResource("black100_pieceonly.png").toExternalForm());
    private Image help = new Image(getClass().getClassLoader().getResource("Help_pieceonly.png").toExternalForm());

    Chip (int chipType, int x, int y) {
            switch (chipType) {
                case 1:
                    placeImage(white, x, y);
                    break;
                case 2:
                    placeImage(black, x, y);
                    break;
                case 3:
                    placeImage(help, x, y);
                default:
                    break;
            }
    }

    private void placeImage(Image image,int x, int y) {
        setImage(image);
        if (image.equals(white) || image.equals(black)) {
            setX(x * MainApp.SQUARE_SIZE);
            setY(y * MainApp.SQUARE_SIZE);
        }
        else {
            setX(x * MainApp.SQUARE_SIZE + 25);
            setY(y * MainApp.SQUARE_SIZE + 25);
        }
        setSmooth(true);
    }
}
