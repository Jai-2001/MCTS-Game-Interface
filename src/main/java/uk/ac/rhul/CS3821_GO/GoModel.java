package uk.ac.rhul.CS3821_GO;

public class GoModel {

    final static int BOARD_SIZE_X = 9;
    final static int BOARD_SIZE_Y = 9;

    private TurnState currentPlayerTurn;
    private Intersection[][] board;
    private int[][] attemptedMove;
    boolean moveWasValid;

    GoModel(){
        this.currentPlayerTurn = new TurnState();
        this.board = new Intersection[BOARD_SIZE_X][BOARD_SIZE_Y];
        for (int x = 0; x < BOARD_SIZE_X; x++) {
            for (int y = 0; y < BOARD_SIZE_Y; y++) {
                this.board[x][y] = new Intersection();
            }
        }
        attemptedMove = null;
        moveWasValid = false;
    }

    public TurnState getCurrentTurn() {
        return this.currentPlayerTurn;
    }

    public void nextTurn() {
        this.currentPlayerTurn.changePlayer();
    }

    public Intersection[][] getBoard(){
        return this.board.clone();
    }

    public boolean tryMove(int xPos, int yPos){
            if (this.currentPlayerTurn.getCurrentPlayer() == TurnState.PLAYER_BLACK){
                board[xPos][yPos].setBlack();
                moveWasValid = true;
                return true;
            }
        moveWasValid = false;
        return false;
    }
}

