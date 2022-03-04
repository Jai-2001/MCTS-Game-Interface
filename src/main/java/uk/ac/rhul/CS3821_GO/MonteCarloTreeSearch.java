package uk.ac.rhul.CS3821_GO;

import java.util.*;
import java.util.stream.Stream;

public class MonteCarloTreeSearch {
    private final int scoreLimit;
    private final double explorationConfidence;
    private final int searchDepth;
    private GoNode shiftingRoot;
    private final GoNode logRoot;
    private final Random rng;
    private final boolean isBlack;
    private final int rollOuts;
    private ArrayList<int[]> moveList;

    public MonteCarloTreeSearch(int scoreLimit, boolean isBlack){
        this(scoreLimit, 50000, 20, 180, new Random(), isBlack);
    }

    public MonteCarloTreeSearch(int scoreLimit, double confidence, int depth, int rollouts, Random rng, boolean isBlack) {
        this.scoreLimit = scoreLimit;
        this.explorationConfidence = confidence;
        this.searchDepth = depth;
        this.rng = rng;
        this.isBlack = isBlack;
        this.rollOuts = rollouts;
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
        GoNode candidate;
        this.moveList.add(taken);
        ArrayList<int[]> moves = new ArrayList<>(this.moveList);
        Stream<GoNode> candidatesStream = this.shiftingRoot.getChildren().stream().filter(
                i -> i.getMoves().subList(0, moves.size()) == moves);
            if(candidatesStream.findFirst().isPresent()){
                candidatesStream = candidatesStream.sorted(Comparator.comparingDouble(GoNode::getVisits));
                candidate = candidatesStream.findFirst().get();
            } else {
                candidate = new GoNode();
                candidate.setMoves(moves);
                this.shiftingRoot.add(candidate);
                candidate.setParent(this.shiftingRoot);
            }
        this.shiftingRoot = candidate;
    }

    public int[] path(){
        GoNode leaf = select(this.shiftingRoot);
        if(leaf.getVisits() != 1){
            do {
                leaf = select(leaf);
            } while (leaf.getVisits() != 1);
        }
            for (int j = 0; j < this.rollOuts; j++) {
                GoModel simModel = presimulate(leaf.getMoves(), this.moveList.size());
                GoNode terminates = rollOut(leaf, simModel);
                backpropagate(leaf, terminates);
            }
        this.shiftingRoot = select(leaf);
        int[] bestMove =  this.shiftingRoot.getMoves().get(this.moveList.size());
        return bestMove;
    }

    public GoNode UCB(GoNode current) {
        double bestScore = Double.NEGATIVE_INFINITY;
        GoNode bestNode = current;
        ArrayList<GoNode> children = current.getChildren();
        for (GoNode child : children) {
            double score =child.getScore() +(this.explorationConfidence * Math.sqrt(Math.log(child.getVisits())/ current.getVisits()));
            if (score > bestScore) {
                bestScore = score;
                bestNode = child;
            }
        }
        return bestNode;
    }

    private GoNode select(GoNode initial) {
        GoNode promising = initial;
        initial = null;
            while(promising.getEndState() == EndStates.RUNNING && initial != promising){
                initial = promising;
                promising = UCB(promising);
            }
        return promising;
    }

    private GoNode rollOut(GoNode start, GoModel simModel) {
        ArrayList<int[]> runningMoves = start.getMoves();
        GoNode ball = start;
        int retryCount = 0;
        int depth = this.searchDepth;
        while (ball.getEndState() == EndStates.RUNNING && depth >= 0){
            int[] newMove = randomMove(simModel);
            ArrayList<int[]> uniqueMoves = new ArrayList<>();
                for (GoNode uniqueChild: start.getChildren()) {
                    uniqueMoves.add(uniqueChild.getMoves().get(this.moveList.size()));
                }
                if(uniqueMoves.stream().anyMatch((int[] i) -> Arrays.equals(i, newMove))){
                    if (retryCount <= this.rollOuts){
                        retryCount++;
                        continue;
                    } else{
                        break;
                    }
                }
            runningMoves = new ArrayList<>(runningMoves);
            runningMoves.add(newMove);
            simModel.nextTurn();
            GoNode nextBall = new GoNode(someoneWon(simModel), null);
            nextBall.setMoves(runningMoves);
            nextBall.setParent(ball);
            ball.add(nextBall);
            ball = nextBall;
            depth--;
        }
        return ball;
    }

    private void backpropagate(GoNode start, GoNode ball) {
        int score = ball.getEndState().ordinal() - 1;
        start.setScore(start.getScore() + score);
        start.incrementVisits();
        while (ball != start){
            ball.setScore(ball.getScore() + score);
            ball = ball.getParent();
            ball.incrementVisits();
        }
    }

    private int[] randomMove(GoModel simModel) {
        int[] move = new int[2];
        int[] indices = this.rng.ints(200,0, 81).distinct().limit(82).toArray();
        for (int index : indices) {
            move = GoLegalMoves.movesArray[index];
            if(move[0] == -1) {
                simModel.moveWasValid = true;
            } else{
                if (simModel.tryMove(move[0],move[1])){
                    break;
                }
            }
        }
        return move;
    }


    public EndStates someoneWon(GoModel model){
        int compIndex = 1;
        int humanIndex = 0;
        if(isBlack){
            compIndex = 0;
            humanIndex = 1;
        }
        int[] points = model.countPoints();
        if(points[compIndex] >= this.scoreLimit){
            return EndStates.WON;
        } else if (points[humanIndex] >= scoreLimit){
            return EndStates.LOST;
        } else{
            return EndStates.RUNNING;
        }
    }
}