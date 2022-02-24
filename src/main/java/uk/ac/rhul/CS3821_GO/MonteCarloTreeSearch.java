package uk.ac.rhul.CS3821_GO;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonteCarloTreeSearch {
    private int scoreLimit;
    private double explorationConfidence;
    private int searchDepth;
    private GoNode shiftingRoot;
    private Random rng;
    private boolean isBlack;
    private int rollOuts;

    public MonteCarloTreeSearch(int scoreLimit, boolean isBlack){
        this(scoreLimit, 1.1, 5, 90, new Random(), isBlack);
    }

    public MonteCarloTreeSearch(int scoreLimit, double confidence, int depth, int rollouts, Random rng, boolean isBlack) {
        this.scoreLimit = scoreLimit;
        this.explorationConfidence = confidence;
        this.searchDepth = depth;
        this.rng = rng;
        this.isBlack = isBlack;
        this.rollOuts = rollouts;
        this.shiftingRoot = new GoNode();
    }

    public void moveTaken(int[] taken){
        GoNode candidate;
        ArrayList<int[]> moves = new ArrayList<>(this.shiftingRoot.getMoves());
        moves.add(taken);
        Stream<GoNode> candidatesStream = this.shiftingRoot.getChildren().stream().filter(
                i -> i.getMoves().subList(0, moves.size() + 1) == moves);
            if(candidatesStream.count() != 0){
                candidatesStream.sorted(Comparator.comparingDouble(GoNode::getScore));
                candidate = candidatesStream.findFirst().get();
            } else {
                candidate = new GoNode();
                candidate.setMoves(moves);
            }
        this.shiftingRoot = candidate;
    }

    public void path(GoModel simModel){
        GoNode leaf = select(this.shiftingRoot);
        if(leaf.getVisits() != 0){
            do {
                leaf = select(leaf);
            } while (leaf.getVisits() != 0 && leaf.getEndState() == EndStates.RUNNING);
        }
        for (int i = 0; i < this.rollOuts; i++) {
            simModel = OnePlayerManager.presimulate(leaf.getMoves(), 0);
            GoNode terminates = rollOut(leaf, simModel);
            backpropagate(leaf, simModel, terminates);
        }
    }

    public GoNode UCB(GoNode current) {
        double bestScore = Double.NEGATIVE_INFINITY;
        GoNode bestNode = current;
        current.incrementVisits();
        ArrayList<GoNode> children = current.getChildren();
        for (GoNode child : children) {
            double score = child.getScore();
            if (score > bestScore) {
                bestScore = score;
                bestNode = child;
            }
        }
        return bestNode;
    }

    private GoNode select(GoNode initial) {
        GoNode promising = initial;
        while(promising.getChildren().size() != 0){
            promising = UCB(promising);
        }
        return promising;
    }

    private GoNode rollOut(GoNode start, GoModel simModel) {
        ArrayList<int[]> runningMoves = start.getMoves();
        GoNode ball = start;
        while (someoneWon(simModel)==EndStates.RUNNING){
            ball.incrementVisits();;
            runningMoves = new ArrayList<>(runningMoves);
            runningMoves.add(randomMove(simModel,ball.getAxioms()));
            simModel.nextTurn();
            GoNode nextBall = new GoNode(someoneWon(simModel), null);
            nextBall.setMoves(runningMoves);
            ball.add(nextBall);
            ball = nextBall;
        }
        return ball;
    }

    private void backpropagate(GoNode start, GoModel simModel, GoNode ball) {
        int score = getDelta(simModel);
        ball.setScore(score);
        GoNode reverseBall = start;
        while (reverseBall != ball){
            reverseBall.setScore(reverseBall.getScore() + score);
            reverseBall = reverseBall.getChildren().get(0);
        }
    }

    private int[] randomMove(GoModel simModel, ArrayList<int[]> axioms) {
        int[] move = new int[2];
        int[] indices = this.rng.ints(200,0, 81).distinct().limit(82).toArray();
        for (int index : indices) {
            move = axioms.get(index);
            if(move != null){
                if (simModel.tryMove(move[0],move[1])){
                    break;
                }
            }
        }
        return move;
    }

    private int getDelta(GoModel model) {
        int compIndex = 1;
        int humanIndex = 0;
        if(isBlack){
            compIndex = 0;
            humanIndex = 1;
        }
        int delta = model.countPoints()[compIndex] - model.countPoints()[humanIndex];
        return delta;
    }

    public EndStates someoneWon(GoModel model){
        int delta = getDelta(model);
        if(delta == 0){
            return EndStates.RUNNING;
        } else if (delta >= scoreLimit){
            return EndStates.WON;
        } else{
            return EndStates.LOST;
        }
    }
}