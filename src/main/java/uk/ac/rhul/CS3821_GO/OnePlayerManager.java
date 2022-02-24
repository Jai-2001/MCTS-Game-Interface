package uk.ac.rhul.CS3821_GO;

import java.util.*;

public class OnePlayerManager extends GoViewController {

    private final boolean isBlack;
    private MonteCarloTreeSearch treeSearch;

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
        this(scoreLimit, isBlack, 1.1, 5, 90, new Random());
    }
    public OnePlayerManager(int scoreLimit, boolean isBlack, double confidence, int depth, int rollOuts, Random rng){
        super();
        this.treeSearch = new MonteCarloTreeSearch(scoreLimit, confidence, depth, rollOuts, rng , isBlack);
        this.isBlack = isBlack;
            if(isBlack){
                play();
                updateBoardState();
            }
    }

    public boolean updateBoardState(){
        int[] move = null;
        if (!(this.passedOnce || this.hasEnded())){
            Intersection wagered = this.model.getWagered();
            move[0] = wagered.getX();
            move[1] = wagered.getY();
        }
        this.treeSearch.moveTaken(move);
        boolean answer = super.updateBoardState();
        if (treeSearch.someoneWon(this.model) == EndStates.LOST){
            System.out.println("You win!");
        } else if (treeSearch.someoneWon(this.model) == EndStates.WON){
            System.out.println("You lost!");
        }
        return answer;
    }

    public void play() {
        this.treeSearch.path(new GoModel());
    }
    public boolean someoneWon(){
       return treeSearch.someoneWon(this.model) != EndStates.RUNNING;
    }

    static GoModel presimulate(ArrayList<int[]> moveList, int limit){
        GoModel simModel = new GoModel();
        limit = limit == 0 ? moveList.size() : limit;
        for (int i = 0; i < limit; i++) {
            int[] move = moveList.get(i);
            if(move == null){
                simModel.nextTurn();
            } else {
                simModel.tryMove(move[0], move[1]);
                simModel.nextTurn();
            }
            simModel.updateLiberties(TurnState.PLAYER_BLACK);
            simModel.updateLiberties(TurnState.PLAYER_BLACK);
        }
        TurnState.flush();
        return simModel;
    }

}
