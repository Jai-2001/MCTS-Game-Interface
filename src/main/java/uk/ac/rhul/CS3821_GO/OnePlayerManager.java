package uk.ac.rhul.CS3821_GO;

import java.util.Random;
import java.util.Scanner;

public class OnePlayerManager extends GoViewController {

    private Random rng;
    private int scoreLimit;


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

    public OnePlayerManager(int scoreLimit, boolean isBlack){
        super();
        this.scoreLimit = scoreLimit;
        this.rng = new Random();
        if(isBlack){
            play();
            updateBoardState();
        }
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

}
