package Othello;

import java.awt.*;

import static Othello.Board.WIDTH;

class CornerPoint {
    private Point move;

    CornerPoint(Point move) {
        this.move = move;
    }
    boolean isCornerPoint() {
        return move.x == 0 && move.y == 0 || move.x == 0 && move.y == WIDTH - 1 || move.x == WIDTH - 1 && move.y == 0 || move.x == WIDTH - 1 && move.y == WIDTH - 1;
    }
}
