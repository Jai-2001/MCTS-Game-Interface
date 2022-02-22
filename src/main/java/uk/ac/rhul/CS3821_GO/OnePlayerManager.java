package uk.ac.rhul.CS3821_GO;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class OnePlayerManager extends GoViewController {

    public static void main(String[] args) {
        int scoreLimit = 5;
        boolean isBlack = true;
        if (args.length == 2){
            scoreLimit = Integer.parseInt(args[0]);
            isBlack = Integer.parseInt(args[0]) == 1;
        }
        OnePlayerManager manager = new OnePlayerManager(scoreLimit, isBlack);
        Scanner inputBuffer = new Scanner(System.in);
        do{
            manager.inputMove(inputBuffer);
            manager.updateBoardState();
            manager.play();
            manager.updateBoardState();
        } while (!manager.someoneWon());
        manager.updateBoardState();
    }

    private Random rng;
    private int scoreLimit;
    private double explorationConfidence = 0.96;
    private int searchDepth = 89;

    public OnePlayerManager(int scoreLimit, boolean isBlack){
        super();
        this.scoreLimit = scoreLimit;
        this.rng = new Random();
        if(isBlack){
            play();
            updateBoardState();
        }
    }

    public OnePlayerManager(int scoreLimit, boolean isBlack, double confidence, int depth){
        this(scoreLimit, isBlack);
        this.explorationConfidence = confidence;
        this.searchDepth = depth;
    }

    public void play(){
       boolean validMove;
            do{
                int x = this.rng.nextInt(this.model.BOARD_SIZE_X);
                int y = this.rng.nextInt(this.model.BOARD_SIZE_Y);
                validMove = this.model.tryMove(x,y);
            } while (!validMove);
    }

    public boolean someoneWon(){
        int[] scores = this.model.countPoints();
        return scores[0] >= this.scoreLimit || scores[1] >= this.scoreLimit;
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
                    double score = sum + (explorationConfidence * (Math.sqrt(Math.log(current.getVisits()))/child.getVisits()));
                    child.setScore(score);
                        if (score > bestScore){
                            bestScore = score;
                            bestNode = child;
                        }
                }
        return bestNode;
    }

    public GoNode path(GoNode root){
        GoNode candidate = root;
        GoNode current = root;
            for (int i = 0; i < this.searchDepth; i++) {
                    if (current != null){
                        candidate = current;
                        current = UCB(current);;
                    } else {
                        current = root;
                    }
            }
        return candidate;
    }

}
