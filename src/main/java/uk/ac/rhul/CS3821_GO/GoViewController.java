package uk.ac.rhul.CS3821_GO;

import java.util.Scanner;

public class GoViewController {

    public static void main(String[] args) {
        GoViewController game = new GoViewController();
        do{
            game.inputMove();
        } while (game.updateBoardState());
    }

    private GoModel model;
    private GoASCIIView view;

    public GoViewController(){
        this(new GoModel(), new GoASCIIView());
    }

    protected GoViewController(GoModel model, GoASCIIView view) {
        this.model = model;
        this.view = view;
    }

    public boolean updateBoardState() {
        if (this.model.moveWasValid){
            this.model.nextTurn();
            return true;
        }
        return false;
    }

    public void inputMove() {

        this.view.printBoard(getIntBoard());
        int moveX = -1;
        int moveY = -1;
        String playerName = "Black";
        if (this.model.getCurrentTurn().getCurrentPlayer() == TurnState.PLAYER_WHITE){
            playerName = "White";
        }
        do {
            String response[] = this.view.promptInput(playerName).split(",");
                if (response[0].equals("q")){
                    this.model.moveWasValid = false;
                    break;
                }
            moveX = Integer.parseInt(response[0])-1;
            moveY = Integer.parseInt(response[1])-1;
        } while(!this.model.tryMove(moveX, moveY));

    }

    public int[][] getIntBoard() {
        Intersection[][] modelBoard = this.model.getBoard();
        int[][] currentBoard = new int[model.BOARD_SIZE_X][model.BOARD_SIZE_Y];
        for (int x = 0; x < model.BOARD_SIZE_X; x++) {
            for (int y = 0; y < model.BOARD_SIZE_Y; y++) {
                currentBoard[x][y] = modelBoard[x][y].getRepresentation();
            }
        }
        return currentBoard;
    }
}
