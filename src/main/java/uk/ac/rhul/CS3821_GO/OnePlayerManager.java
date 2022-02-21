package uk.ac.rhul.CS3821_GO;

import java.util.Random;

public class OnePlayerManager extends GoViewController {

    private Random rng;

    public OnePlayerManager(int scoreLimit, boolean isBlack){
        super();
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

}
