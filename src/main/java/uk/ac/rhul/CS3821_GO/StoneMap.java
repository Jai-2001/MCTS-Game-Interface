package uk.ac.rhul.CS3821_GO;

public class StoneMap {
    private Intersection[][] grid;
    private Intersection wagered;
    private PlayerModel currentPlayer;
    StoneMap(int xSize, int ySize){
        this.grid = new Intersection[xSize][ySize];
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                this.grid[x][y] = new Intersection();
            }
        }
        this.wagered = null;
        this.currentPlayer = null;
    }

    public boolean checkMove(int xPos, int yPos, PlayerModel currentPlayer) {
        this.wagered = this.grid[xPos][yPos];
        this.currentPlayer = currentPlayer;
        return this.wagered.isCleared();
    }

    public void confirmMove(){
        if(currentPlayer == TurnState.PLAYER_WHITE){
            this.wagered.setWhite();
        } else{
            this.wagered.setBlack();
        }
    }

    public Intersection getStone(int xPos, int yPos){
        return this.grid[xPos][yPos];
    }

    public Intersection[][] copy(){
        return grid;
    }
}
