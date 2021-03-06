package Othello;

import java.util.ArrayList;
import java.util.Iterator;

    class Bot {

        Coordinates nextTurn(Board currentBoard) {
            Minimax _rootState = new Minimax(currentBoard);
            int score = _rootState.negascount();
            System.out.println("Chance of Victory: " + score);
            return getMove(score, _rootState.getNodes());
        }

        private Coordinates getMove(int move, ArrayList<Minimax> children) {
            move *= -1;
            if (children.isEmpty()) {
                return null;
            }
            Iterator<Minimax> i = children.iterator();
            Coordinates p = null;

            while (i.hasNext()) {
                Minimax node = i.next();
                if (node.getKey() == move ) {
                    p = node.getChipPlace();
                    break;
                }
            }
            return p;
        }
}
