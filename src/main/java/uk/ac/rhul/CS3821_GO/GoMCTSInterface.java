package uk.ac.rhul.CS3821_GO;

import java.util.*;
import java.util.stream.Stream;

public class GoMCTSInterface implements GameMCTSInterface {
    private final boolean compIsBlack;
    private final int scoreLimit;
    Random rng;

    public GoMCTSInterface(boolean compIsBlack, int scoreLimit, Random rng) {
        this.compIsBlack = compIsBlack;
        this.scoreLimit = scoreLimit;
        this.rng = rng;
    }

    @Override
    public GameModel presimulate(List<byte[]> moveList){
        return presimulate(moveList, 0);
    }
    @Override
    public GameModel presimulate(List<byte[]> moveList, int limit) {
        return simulate(moveList,limit);
    }

    public static GoModel simulate(List<byte[]> moveList, int limit){
        GoModel simModel = new GoModel();
        limit = limit == 0 ? moveList.size() : limit;
        for (int i = 0; i < limit; i++) {
            byte[] move = moveList.get(i);
                if (move[0] > -1) {
                    simModel.tryMove(move[0], move[1]);
                }
                simModel.nextTurn();
        }
        return simModel;
    }

    @Override
    public byte[][] getRepresentation(List<byte[]> moveList) {
        return represent(moveList);
    }

    public static byte[][] represent(List<byte[]> moveList){
        return GoViewController.getIntBoard(simulate(moveList,0).getBoard());
    }

    @Override
    public byte[] randomMove(GameModel simModel) {
        byte[] move;
            do {
                move = GoLegalMoves.movesArray[(int) (this.rng.nextDouble() * 82)];
            } while (!simModel.tryMove(move[0], move[1]));
        return move;
    }

    @Override
    public int[] indices() {
        int compIndex = 1;
        int humanIndex = 0;
            if (this.compIsBlack) {
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
            if (points[indices[0]] >= limit) {
                return EndStates.WON;
            } else if (points[indices[1]] >= limit || (model.hasEnded())) {
                return EndStates.LOST;
            } else {
                return EndStates.RUNNING;
            }
    }


}