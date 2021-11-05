package uk.ac.rhul.CS3821_GO;

public class GoModel {

    final static int BOARD_SIZE_X = 9;
    final static int BOARD_SIZE_Y = 9;

    private TurnState currentPlayerTurn;

    GoModel(){
        this.currentPlayerTurn = new TurnState();
    }

    public TurnState getCurrentTurn() {
        return this.currentPlayerTurn;
    }

    public void nextTurn() {
        this.currentPlayerTurn.changePlayer();
    }

    public int[][] getBoard() {
        return new int[BOARD_SIZE_X][BOARD_SIZE_Y];
    }

    public boolean tryMove(int xPos, int yPos){
        return this.currentPlayerTurn.getCurrentPlayer() == TurnState.PLAYER_BLACK;
    }
}

