package Othello;

import static Othello.Board.WIDTH;

class CornerPoint {
    private Coordinates move;

    CornerPoint(Coordinates move) {
        this.move = move;
    }
    boolean isCornerPoint() {
        return move.getX() == 0 && move.getY() == 0 || move.getX() == 0 && move.getY() == WIDTH - 1 || move.getX() == WIDTH - 1 && move.getY() == 0 || move.getX() == WIDTH - 1 && move.getY() == WIDTH - 1;
    }
}
