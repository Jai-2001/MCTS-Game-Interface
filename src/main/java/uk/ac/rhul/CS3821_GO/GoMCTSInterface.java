package uk.ac.rhul.CS3821_GO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class GoMCTSInterface implements GameMCTSInterface {
    private final boolean isBlack;
    private final int scoreLimit;
    Random rng;

    public GoMCTSInterface(boolean isBlack, int scoreLimit, Random rng) {
        this.isBlack = isBlack;
        this.scoreLimit = scoreLimit;
        this.rng = rng;
    }

    @Override
    public GameModel presimulate(ArrayList<int[]> moveList, int limit) {
        GoModel simModel = new GoModel();
        limit = limit == 0 ? moveList.size() : limit;
        for (int i = 0; i < limit; i++) {
            int[] move = moveList.get(i);
            if (move[0] != -1) {
                simModel.tryMove(move[0], move[1]);
            }
            simModel.nextTurn();
        }
        return simModel;
    }


    @Override
    public int[] randomMove(GameModel simModel) {
        int[] move;
            do {
                move = GoLegalMoves.movesArray[(int) (this.rng.nextDouble() * 82)];
            } while (!simModel.tryMove(move[0], move[1]));
        return move;
    }

    @Override
    public int[] indices() {
        int compIndex = 1;
        int humanIndex = 0;
            if (this.isBlack) {
                compIndex = 0;
                humanIndex = 1;
            }
        return new int[]{compIndex, humanIndex};
    }

    @Override
    public EndStates someoneWon(GameModel model) {
        int[] indices = indices();
        int[] points = model.countPoints();
        int limit = this.scoreLimit;
            if(model.hasEnded()){
                limit = 0;
            }
            if (points[indices[0]] >= limit) {
                return EndStates.WON;
            } else if (points[indices[1]] >= limit || model.hasEnded()) {
                return EndStates.LOST;
            } else {
                return EndStates.RUNNING;
            }
    }
}