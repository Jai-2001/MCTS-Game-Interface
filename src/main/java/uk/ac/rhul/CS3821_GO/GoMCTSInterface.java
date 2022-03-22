package uk.ac.rhul.CS3821_GO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class GoMCTSInterface implements GameMCTSInterface {
    private final MonteCarloTreeSearch monteCarloTreeSearch;
    private final boolean isBlack;
    private final int scoreLimit;
    Random rng;

    public GoMCTSInterface(MonteCarloTreeSearch monteCarloTreeSearch, boolean isBlack, int scoreLimit, Random rng) {
        this.monteCarloTreeSearch = monteCarloTreeSearch;
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
    public void moveTaken(int[] taken) {
        MCTSNode candidate;
        monteCarloTreeSearch.getMoveList().add(taken);
        HashSet<MCTSNode> falsePredictions = new HashSet<>();
        ArrayList<int[]> moves = new ArrayList<>(monteCarloTreeSearch.getMoveList());
        next:
            for (MCTSNode i : monteCarloTreeSearch.getShiftingRoot().getChildren()) {
                List<int[]> subMoves = i.getMoves().subList(0, moves.size());
                    for (int j = 0; j < subMoves.size(); j++) {
                            if (subMoves.get(j)[0] != moves.get(j)[0] || subMoves.get(j)[1] != moves.get(j)[1]) {
                                falsePredictions.add(i);
                                continue next;
                            }
                    }
            }
        monteCarloTreeSearch.getShiftingRoot().getChildren().removeAll(falsePredictions);
        candidate = monteCarloTreeSearch.select(monteCarloTreeSearch.getShiftingRoot());
            if (candidate == monteCarloTreeSearch.getShiftingRoot()) {
                candidate = new MCTSNode();
                candidate.setMoves(moves);
                monteCarloTreeSearch.getShiftingRoot().add(candidate);
            }
        candidate.setParent(null);
        monteCarloTreeSearch.setShiftingRoot(candidate);
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
            if (points[indices[1]] >= this.scoreLimit) {
                return EndStates.LOST;
            } else if (points[indices[0]] >= this.scoreLimit) {
                return EndStates.WON;
            } else {
                return EndStates.RUNNING;
            }
    }
}