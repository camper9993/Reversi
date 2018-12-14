package Othello;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


import static Othello.ChipType.BLACK;
import static sun.swing.MenuItemLayoutHelper.max;


class Minimax {
    private static final int DEAD_END = Integer.MIN_VALUE/4;
    private static final int END_GAME_WIN = Integer.MAX_VALUE;
    private static final int END_GAME_LOSE = Integer.MIN_VALUE + 1;
    private static final int END_GAME_TIE = Integer.MIN_VALUE/2;
    private static final int maxDepth = 4; //5 - сложно,  4 - средне, 3 - легко

    private Board board;
    private int key;
    private ArrayList<Minimax> nodes;
    private ChipType currentPlayer;
    private Point chipPlace;
    private int depth;

    Minimax(Board board) {
        depth = 0;
        this.board = board.cloneBoard();
        nodes = new ArrayList<>();
        chipPlace = null;
        currentPlayer = board.getCurrentPlayer();
    }

    private Minimax(Board board,int depth, Point chipPlace) {
        this.board = board.cloneBoard();
        this.depth = depth;
        nodes = new ArrayList<>();
        this.chipPlace = chipPlace;
        currentPlayer = board.getCurrentPlayer();
    }

    int negascount() {
        return negascount(END_GAME_LOSE,END_GAME_WIN);
    }

    private int negascount(int alpha, int beta) {
        int a = alpha;
        int b = beta;
        int score;
        if (depth > 0) {
            board.updateBoard(chipPlace.x,chipPlace.y);
        }
        List<Point> availableMoves = board.getPossibleMoves();
        if (depth == maxDepth || availableMoves.isEmpty()) {
            key = evaluate();
            return key;
        }
        for (Point move: availableMoves) {
            Minimax node = new Minimax(board,depth + 1,move);
            nodes.add(node);
            score = -1 * node.negascount(-1 * b,-1 * a);
            if (score > a && b != beta && depth >= maxDepth - 2)
                score = -1 * node.negascount(-1 * beta, -1 * score);
            a = max(a,score);
            if (a >= beta) {
                key = a;
                return a;
            }
            b = a + 1;
        }
        key = a;
        return a;
    }

    private int evaluate() {
        board.removeHelp();
        board.checkPossibleTurns(board.getCurrentPlayer());
        List<Point> moves = board.getPossibleMoves();
        board.checkPossibleTurns(board.getOpponent());
        List<Point> opponentMoves = board.getPossibleMoves();
        int blackScore = board.getBlackScore();
        int whiteScore = board.getWhiteScore();
        if (!opponentMoves.isEmpty() && moves.isEmpty()) {
            key = DEAD_END;
        }
        else if (opponentMoves.isEmpty() && moves.isEmpty()) {
            if ((blackScore > whiteScore && currentPlayer == BLACK) || (blackScore < whiteScore && currentPlayer == ChipType.WHITE))
                key = END_GAME_WIN;
            else if ((blackScore > whiteScore && currentPlayer == ChipType.WHITE) || (blackScore < whiteScore && currentPlayer == BLACK))
                key = END_GAME_LOSE;
            else key = END_GAME_TIE;
        }
        else {
            if (currentPlayer == BLACK) {
                key = blackScore - whiteScore;
                }
            else {
                key = whiteScore - blackScore;
            }
        }
        return key;
    }

    int getKey() {
        return key;
    }
    Point getChipPlace() {
        return chipPlace;
    }

    ArrayList<Minimax> getNodes() {
        return nodes;
    }
}
