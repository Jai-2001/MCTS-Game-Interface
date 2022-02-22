package uk.ac.rhul.CS3821_GO;

import java.util.ArrayList;

public class MonteCarloTreeSearch {
    private int scoreLimit;
    private double explorationConfidence;
    private int searchDepth;

    public MonteCarloTreeSearch(int scoreLimit, double confidence, int depth) {
        this.scoreLimit = scoreLimit;
        this.explorationConfidence = confidence;
        this.searchDepth = depth;
    }

    public GoNode UCB(GoNode current) {
        double bestScore = Double.MIN_VALUE;
        double sum;
        GoNode bestNode = null;
        current.incrementVisits();
        ArrayList<GoNode> children = current.getChildren();
        for (GoNode child : children) {
            sum = child.getScore();
            child.incrementVisits();
            EndStates endState = child.getEndState();
            if (endState == EndStates.WON) {
                sum++;
            } else if (endState == EndStates.LOST) {
                sum--;
            }
            double score = sum + (explorationConfidence * (Math.sqrt(Math.log(current.getVisits())) / child.getVisits()));
            child.setScore(score);
            if (score > bestScore) {
                bestScore = score;
                bestNode = child;
            }
        }
        return bestNode;
    }

    public GoNode path(GoNode root) {
        GoNode candidate = root;
        GoNode current = root;
        for (int i = 0; i < this.searchDepth; i++) {
            if (current != null) {
                candidate = current;
                current = UCB(current);
                ;
            } else {
                current = root;
            }
        }
        return candidate;
    }
}