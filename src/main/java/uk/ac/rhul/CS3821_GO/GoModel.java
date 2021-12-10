package uk.ac.rhul.CS3821_GO;

public class GoModel {

    final static int BOARD_SIZE_X = 9;
    final static int BOARD_SIZE_Y = 9;

    private TurnState currentPlayerTurn;
    private StoneMap board;
    private int[] attemptedMove;
    boolean moveWasValid;

    GoModel(){
        this.currentPlayerTurn = new TurnState();
        this.board = new StoneMap(BOARD_SIZE_X, BOARD_SIZE_Y);
        attemptedMove = new int[2];
        moveWasValid = false;
    }

    public TurnState getCurrentTurn() {
        return this.currentPlayerTurn;
    }

    public void nextTurn() {
        Intersection relevant = this.board.getStone(attemptedMove[0],attemptedMove[1]);
        PlayerModel currentPlayer = this.currentPlayerTurn.getCurrentPlayer();
        if (moveWasValid){
            this.board.confirmMove();
            this.currentPlayerTurn.changePlayer();
        }

    }

    public Intersection[][] getBoard() {
        return this.board.copy();
    }

    public boolean tryMove(int xPos, int yPos){
        if (this.board.checkMove(xPos, yPos, currentPlayerTurn)){
            this.moveWasValid = true;
            return true;
        }
        this.moveWasValid = false;
        return false;
    }
}

