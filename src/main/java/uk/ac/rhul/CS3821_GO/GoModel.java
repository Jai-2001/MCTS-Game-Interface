package uk.ac.rhul.CS3821_GO;

public class GoModel {

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
        return new int[9][9];
    }
}

