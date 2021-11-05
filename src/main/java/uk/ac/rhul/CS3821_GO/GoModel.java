package uk.ac.rhul.CS3821_GO;

public class GoModel {

    final static int BOARD_SIZE_X = 9;
    final static int BOARD_SIZE_Y = 9;

    private TurnState currentPlayerTurn;
    private Intersection[][] board;
    private int[] attemptedMove;
    boolean moveWasValid;

    GoModel(){
        this.currentPlayerTurn = new TurnState();
        this.board = new Intersection[BOARD_SIZE_X][BOARD_SIZE_Y];
        for (int x = 0; x < BOARD_SIZE_X; x++) {
            for (int y = 0; y < BOARD_SIZE_Y; y++) {
                this.board[x][y] = new Intersection();
            }
        }
        attemptedMove = new int[2];
        moveWasValid = false;
    }

    public TurnState getCurrentTurn() {
        return this.currentPlayerTurn;
    }

    public void nextTurn() {
        Intersection relevant = this.board[attemptedMove[0]][attemptedMove[1]];
        PlayerModel currentPlayer = this.currentPlayerTurn.getCurrentPlayer();
        if (moveWasValid){
            if(currentPlayer == TurnState.PLAYER_BLACK){
                relevant.setBlack();
            } else if(currentPlayer == TurnState.PLAYER_WHITE){
                relevant.setWhite();
            }
            this.currentPlayerTurn.changePlayer();
        }

    }

    public Intersection[][] getBoard(){
        return this.board.clone();
    }

    public boolean tryMove(int xPos, int yPos){
        attemptedMove[0] = xPos;
        attemptedMove[1] = yPos;
            if (this.board[xPos][yPos].isCleared()){
                this.moveWasValid = true;
                return true;
            }
        this.moveWasValid = false;
        return false;
    }
}

