package uk.ac.rhul.CS3821_GO;

import java.util.Scanner;

public class GoViewController {

    public static void main(String[] args) {
        GoViewController game = new GoViewController();
        Scanner inputBuffer = new Scanner(System.in);
            do{
                game.inputMove(inputBuffer);
            } while (game.updateBoardState() && !game.hasEnded());
    }

    protected GoModel model;
    View view;


    public GoViewController(){
        this(new GoModel(), new GoASCIIView());
    }

    public GoViewController(GoModel model, View view) {
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

    public void inputMove(Scanner inputBuffer) {
        this.view.printBoard(getIntBoard());
        int moveX;
        int moveY;
        String playerName = "Black";
        TurnState turn = this.model.getCurrentTurn();
        if (turn.getCurrentPlayer() == turn.getWhite()){
            playerName = "White";
        }
        game: do {
            int[] intScores = this.model.countPoints();
            String[] scores = {Integer.toString(intScores[0]), Integer.toString(intScores[1])};
            String[] response = this.view.promptInput(playerName, scores, inputBuffer).split(",");
                switch (response[0].charAt(0)){
                    case 'q':
                        for (int i = 0; i < 3; i++) {
                            this.model.tryMove(-1,1);
                            this.model.nextTurn();
                        }
                    case 'p':
                        this.model.tryMove(-1,1);
                        continue;
                    default:
                        moveX = Integer.parseInt(response[0])-1;
                        moveY = Integer.parseInt(response[1])-1;
                        this.model.tryMove(moveY,moveX);
                        break;
                }
         } while(!this.model.moveWasValid);

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

    public boolean hasEnded() {
        return this.model.hasEnded();
    }
}
