package uk.ac.rhul.CS3821_GO;

public class GoModel {

    final static int BOARD_SIZE_X = 9;
    final static int BOARD_SIZE_Y = 9;

    private final TurnState currentPlayerTurn;
    private final StoneMap board;
    boolean moveWasValid;

    GoModel(){
        this.currentPlayerTurn = new TurnState();
        this.board = new StoneMap(BOARD_SIZE_X, BOARD_SIZE_Y);
        moveWasValid = false;
    }

    public TurnState getCurrentTurn() {
        return this.currentPlayerTurn;
    }

    public void nextTurn() {
        if (moveWasValid){
            this.board.confirmMove();
            this.currentPlayerTurn.changePlayer();
        }

    }

    public Intersection[][] getBoard() {
        return this.board.copy();
    }

    public boolean tryMove(int xPos, int yPos){
            if (this.board.checkMove(xPos, yPos, currentPlayerTurn.getCurrentPlayer())){
                this.moveWasValid = true;
                return true;
            }
        this.moveWasValid = false;
        return false;
    }
}

