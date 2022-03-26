package uk.ac.rhul.CS3821_GO;

import java.util.*;

public class OnePlayerManager extends GoViewController {

    private final MonteCarloTreeSearch treeSearch;

    public static void main(String[] args) {
        int scoreLimit = 5;
        boolean isBlack = true;
        if (args.length == 2){
            scoreLimit = Integer.parseInt(args[0]);
            isBlack = Integer.parseInt(args[1]) == 1;
        }
        OnePlayerManager manager = new OnePlayerManager(scoreLimit, isBlack);
        Scanner inputBuffer = new Scanner(System.in);
        do{
            manager.inputMove(inputBuffer);
            manager.updateBoardState();
            manager.play();
            manager.updateBoardState();
        } while (!manager.someoneWon() &&!manager.hasEnded());
        manager.updateBoardState();
    }

    public OnePlayerManager(int scoreLimit, boolean isBlack) {
        this(scoreLimit, isBlack, Math.sqrt(2.0), 450, 35,350,  new Random());//ASCII uses this
    }
    public OnePlayerManager(int scoreLimit, boolean isBlack, View view) {
        this(scoreLimit, isBlack, Math.sqrt(2.0), 450, 35, 350, new Random());//GUI uses this
        this.view = view;
    }
    public OnePlayerManager(int scoreLimit, boolean isBlack, double confidence, int depth, int rollOuts, int iterations, Random rng){
        super();
        this.treeSearch = new MonteCarloTreeSearch(scoreLimit, confidence, depth, rollOuts, rng , isBlack, iterations);//Tests use this
            if(isBlack){
                play();
                updateBoardState();
            }
    }

    @Override
    public void inputMove(Scanner inputBuffer){
        super.inputMove(inputBuffer);
    }

    @Override
    public boolean updateBoardState(){
        Intersection wagered = this.model.getWagered();
            if(wagered!=null) {
                this.treeSearch.moveTaken(new int[]{wagered.getX(), wagered.getY()});
            }
            if (treeSearch.someoneWon(this.model) == EndStates.LOST){
                System.out.println("You win!");
            } else if (treeSearch.someoneWon(this.model) == EndStates.WON){
                System.out.println("You lost!");
            }
        return super.updateBoardState();
    }

    public void play() {
        boolean track;
        do {
            int[] compMove = this.treeSearch.path();
                track = this.model.tryMove(compMove[0], compMove[1]);
        } while(!track);
    }
    public boolean someoneWon(){
       return treeSearch.someoneWon(this.model) != EndStates.RUNNING;
    }

}
