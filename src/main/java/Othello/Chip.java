package Othello;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



class Chip extends ImageView {
    private Image white = new Image(getClass().getClassLoader().getResource("white100_pieceonly.png").toExternalForm());
    private Image black = new Image(getClass().getClassLoader().getResource("black100_pieceonly.png").toExternalForm());
    private Image help = new Image(getClass().getClassLoader().getResource("Help_pieceonly.png").toExternalForm());

    Chip (ChipType chipType, int x, int y) {
            switch (chipType) {
                case WHITE:
                    placeImage(white, x, y);
                    break;
                case BLACK:
                    placeImage(black, x, y);
                    break;
                case HELP:
                    placeImage(help, x, y);
                default:
                    break;
            }
    }

    private void placeImage(Image image,int x, int y) {
        setImage(image);
        if (image.equals(white) || image.equals(black)) {
            setX(x * Board.SQUARE_SIZE);
            setY(y * Board.SQUARE_SIZE);
        }
        else {
            setX(x * Board.SQUARE_SIZE + 25);
            setY(y * Board.SQUARE_SIZE + 25);
        }
        setSmooth(true);
    }
}
