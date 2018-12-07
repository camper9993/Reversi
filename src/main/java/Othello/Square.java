package Othello;

import javafx.scene.paint.Color;

import javafx.scene.shape.Rectangle;

class Square extends Rectangle{
    Square(boolean color, int x, int y) {
        setWidth(Board.SQUARE_SIZE);
        setHeight(Board.SQUARE_SIZE);
        relocate(x * Board.SQUARE_SIZE,y * Board.SQUARE_SIZE);
        setFill(color ? Color.rgb(0,100,0) : Color.rgb(0,128,0));
    }

}
