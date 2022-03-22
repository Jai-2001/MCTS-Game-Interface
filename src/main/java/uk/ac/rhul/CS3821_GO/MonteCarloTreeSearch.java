package uk.ac.rhul.CS3821_GO;

import java.util.*;
import java.util.stream.Stream;

import static uk.ac.rhul.CS3821_GO.EndStates.*;

public class MonteCarloTreeSearch {
    private final int scoreLimit;
    private final double explorationConfidence;
    private final int searchDepth;
    private GoNode shiftingRoot;
    private final GoNode logRoot;
    private final Random rng;
    private final boolean isBlack;
    private final int rollOuts;
    private final int iterations;
    private ArrayList<int[]> moveList;

    public MonteCarloTreeSearch(int scoreLimit, boolean isBlack){
        this(scoreLimit, 50000, 20, 180, new Random(), isBlack, 5);
    }

    public MonteCarloTreeSearch(int scoreLimit, double confidence, int depth, int rollouts, Random rng, boolean isBlack, int iterations) {
        this.scoreLimit = scoreLimit;
        this.explorationConfidence = confidence;
        this.searchDepth = depth;
        this.rng = rng;
        this.isBlack = isBlack;
        this.rollOuts = rollouts;
        this.iterations = iterations;
        this.logRoot = new GoNode();
        this.shiftingRoot = logRoot;
        this.moveList = new ArrayList<>();
    }

    static GoModel presimulate(ArrayList<int[]> moveList, int limit){
        GoModel simModel = new GoModel();
        limit = limit == 0 ? moveList.size() : limit;
        for (int i = 0; i < limit; i++) {
            int[] move = moveList.get(i);
            if(move[0] != -1){
                simModel.tryMove(move[0], move[1]);
            }
            simModel.nextTurn();
        }
        return simModel;
    }

    public void moveTaken(int[] taken){
        GoNode candidate = null;
        this.moveList.add(taken);
        HashSet<GoNode> falsePredictions = new HashSet<>();
        ArrayList<int[]> moves = new ArrayList<>(this.moveList);
            next: for (GoNode i : this.shiftingRoot.getChildren()) {
                List<int[]> subMoves = i.getMoves().subList(0, moves.size());
                    for (int j = 0; j < subMoves.size(); j++) {
                        if (subMoves.get(j)[0] != moves.get(j)[0] || subMoves.get(j)[1] != moves.get(j)[1]) {
                            falsePredictions.add(i);
                            continue next;
                        }
                    }
            }
            this.shiftingRoot.getChildren().removeAll(falsePredictions);
            candidate = select(this.shiftingRoot);
            if(candidate == this.shiftingRoot){
                candidate = new GoNode();
                candidate.setMoves(moves);
                this.shiftingRoot.add(candidate);
                candidate.setParent(this.shiftingRoot);
            }
        this.shiftingRoot = candidate;
    }

    public int[] path(){
        GoNode leaf;
            for (int i = 0; i < iterations; i++) {
                leaf = UCB(this.shiftingRoot);
                GoNode lastLeaf;
                if (leaf.getVisits() <= this.moveList.size()) {
                    do {
                        lastLeaf = leaf;
                        leaf = select(leaf);
                    } while (leaf.getVisits() != 0 && leaf != lastLeaf);
                }
                for (int j = 0; j < this.rollOuts; j++) {
                    GoModel simModel = presimulate(leaf.getMoves(), this.moveList.size());
                    GoNode terminates = rollOut(leaf, simModel);
                    if (terminates != leaf){
                        backpropagate(this.shiftingRoot, terminates, simModel);
                    }
                }
//                System.out.printf("\n%d\n", i);
            }
        leaf = select(this.shiftingRoot);
        int[] bestMove =  leaf.getMoves().get(this.moveList.size());
        return bestMove;
    }

    public GoNode UCB(GoNode current) {
        double bestScore = Integer.MIN_VALUE;
        GoNode bestNode = current;
        ArrayList<GoNode> children = current.getChildren();
        Collections.shuffle(children);
        for (GoNode child : children) {
            double score = child.getScore();
            double ratio = Math.log(current.getVisits())/child.getVisits();
//            if (child.getVisits() == 0){
//                ratio = Double.POSITIVE_INFINITY;  //division by 0 is NaN, but we want to treat 1/0 as infinite
//                score = 0;
//            }
            score += (this.explorationConfidence * Math.sqrt(ratio));

            if (score > bestScore) {
                bestScore = score;
                bestNode = child;
            }
        }
        return bestNode;
    }

    private GoNode select(GoNode initial) {
        GoNode selection = initial;
        Collections.shuffle(selection.getChildren(), this.rng);
        for (GoNode promising: initial.getChildren()) {
            if(promising.getEndState() == WON) {
                return promising;
            }
        }
        return UCB(initial);
    }

    private GoNode rollOut(GoNode start, GoModel simModel) {
        ArrayList<int[]> runningMoves = start.getMoves();
        GoNode ball = start;
        int depth = this.searchDepth;
        deep: while (ball.getEndState() == RUNNING && depth >= 0){

            ArrayList<int[]> uniqueMoves = new ArrayList<>();
                for (GoNode uniqueChild: start.getChildren()) {
                    uniqueMoves.add(uniqueChild.getMoves().get(start.getMoves().size()));
                }
            int[] newMove;
            boolean hasBeenDone;
            int tries = 0;
            do {
                hasBeenDone = false;
                newMove = randomMove(simModel);
                for (int[] unique : uniqueMoves) {
                    if (unique[0] == newMove[0] && unique[1] == newMove[1]) {
                        hasBeenDone = true;
                        break;
                    }
                }
                tries++;
                if(tries > this.rollOuts){
                    GoNode cannon = ball;
                    while(cannon != start){
                        cannon = cannon.getParent();
                        if (cannon != null) cannon.getChildren().removeIf((i)->true);
                    }
                    return start;
                }
            } while (hasBeenDone);
            runningMoves = new ArrayList<>(runningMoves);
            runningMoves.add(newMove);
            simModel.nextTurn();
            GoNode nextBall = new GoNode(someoneWon(simModel), null);
            nextBall.setMoves(runningMoves);
            nextBall.setParent(ball);
            ball.incrementVisits();
            ball.add(nextBall);
            ball = nextBall;
            depth--;
        }
        return ball;
    }

    private void backpropagate(GoNode start, GoNode ball, GoModel simModel) {
        start.incrementVisits();
        if(ball.getEndState()!= RUNNING) {
            double score;
                switch (ball.getEndState()){
                    case WON : score = 1;
                    break;
                    case LOST: score = -100;
                    break;
                    default: score = 0;
                }
            start.setScore(start.getScore() + score);
            while (ball != start) {
                ball.incrementVisits();
                ball.setScore(ball.getScore() + score);
                ball = ball.getParent();
            }
        }
    }

    private int[] randomMove(GoModel simModel) {
        int[] move;
            do{
                move = GoLegalMoves.movesArray[(int) (this.rng.nextDouble() * 82)];
            } while (!simModel.tryMove(move[0], move[1]));
        return move;
    }

    private int[] indices(){
        int compIndex = 1;
        int humanIndex = 0;
        if(isBlack){
            compIndex = 0;
            humanIndex = 1;
        }
        return new int[]{compIndex,humanIndex};
    }

    public EndStates someoneWon(GoModel model){
        int[] indices = indices();
        int[] points = model.countPoints();
        if(points[indices[1]] >= scoreLimit){
            return LOST;
        } else if (points[indices[0]] >= scoreLimit){
            return WON;
        } else{
            return RUNNING;
        }
    }
}