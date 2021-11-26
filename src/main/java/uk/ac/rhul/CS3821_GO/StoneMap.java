package uk.ac.rhul.CS3821_GO;

public class StoneMap {
    private Intersection[][] grid;

    StoneMap(int xSize, int ySize){
        this.grid = new Intersection[xSize][ySize];
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                this.grid[x][y] = new Intersection();
            }
        }
    }

    public boolean checkMove(int xPos, int yPos, PlayerModel currentPlayer) {
        return this.grid[xPos][yPos].isCleared();
    }

    public Intersection getStone(int xPos, int yPos){
        return this.grid[xPos][yPos];
    }

    public Intersection[][] copy(){
        return grid;
    }
}
