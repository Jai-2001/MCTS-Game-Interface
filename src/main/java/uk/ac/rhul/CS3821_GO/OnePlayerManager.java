package uk.ac.rhul.CS3821_GO;

import java.util.*;

public class OnePlayerManager extends GoViewController {

    private MonteCarloTreeSearch treeSearch;
    private boolean playerMoved;

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

    public OnePlayerManager(int scoreLimit, boolean isBlack) {
        this(scoreLimit, isBlack, 10000000, 5, 82, new Random());
    }
    public OnePlayerManager(int scoreLimit, boolean isBlack, double confidence, int depth, int rollOuts, Random rng){
        super();
        this.playerMoved = true;
        this.treeSearch = new MonteCarloTreeSearch(scoreLimit, confidence, depth, rollOuts, rng , isBlack);
            if(isBlack){
                play();
                updateBoardState();
            }
    }

    @Override
    public void inputMove(Scanner inputBuffer){
        super.inputMove(inputBuffer);
        this.playerMoved = true;
    }

    @Override
    public boolean updateBoardState(){
        Intersection wagered = this.model.getWagered();
            if (wagered.getX() == -1) {
                this.treeSearch.moveTaken(new int[]{-1,-1});
                this.model.nextTurn();
                this.model.moveWasValid = false;
            } else {
                this.treeSearch.moveTaken(new int[]{wagered.getY(), wagered.getX()});
            }
            if (treeSearch.someoneWon(this.model) == EndStates.LOST){
                System.out.println("You win!");
            } else if (treeSearch.someoneWon(this.model) == EndStates.WON){
                System.out.println("You lost!");
            }
        return super.updateBoardState();
    }

    public void play() {
        int[] compMove = this.treeSearch.path();
        this.playerMoved = false;
        if (compMove[0] != -1){
            this.model.tryMove(compMove[0], compMove[1]);
        }
    }
    public boolean someoneWon(){
       return treeSearch.someoneWon(this.model) != EndStates.RUNNING;
    }

}
