package uk.ac.rhul.CS3821_GO;

public class StoneMap {
    private Intersection[][] board;

    StoneMap(int xSize, int ySize){
        this.board = new Intersection[xSize][ySize];
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                this.board[x][y] = new Intersection();
            }
        }
    }

    public Intersection getStone(int xPos, int yPos){
        return this.board[xPos][yPos];
    }

    public Intersection[][] copy(){
        return board;
    }
}
