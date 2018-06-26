package Othello;

import javafx.scene.paint.Color;

import javafx.scene.shape.Rectangle;

class Square extends Rectangle{
    Square(boolean color, int x, int y) {
        setWidth(MainApp.SQUARE_SIZE);
        setHeight(MainApp.SQUARE_SIZE);
        relocate(x * MainApp.SQUARE_SIZE,y * MainApp.SQUARE_SIZE);
        setFill(color ? Color.rgb(0,100,0) : Color.rgb(0,128,0));
    }

}
