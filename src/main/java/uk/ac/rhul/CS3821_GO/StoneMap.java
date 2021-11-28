package uk.ac.rhul.CS3821_GO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StoneMap {

    private final Intersection[][] grid;
    private Intersection wagered;
    private PlayerModel currentPlayer;

    public static final int[][] OFFSETS_ARRAY = {{0,-1},{0,1},{-1,0},{1,0}};
    public static final Set<int[]> STANDARD_OFFSETS = new HashSet<>(Arrays.asList(OFFSETS_ARRAY));

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

    static Set<int[]> prepareOffsets(int xPos, int yPos){
        Set<int[]> boundedOffsets = new HashSet<>();
        for (int[] offset: STANDARD_OFFSETS) {
            int newX = xPos + offset[0];
            int newY = yPos + offset[1];
            if(newX >= 0 && newX < GoModel.BOARD_SIZE_X && newY >= 0 && newY < GoModel.BOARD_SIZE_Y){
                boundedOffsets.add(offset);
            }
        }
        return boundedOffsets;
    }

    public boolean checkMove(int xPos, int yPos, PlayerModel currentPlayer) {
        this.wagered = this.grid[xPos][yPos];
        this.currentPlayer = currentPlayer;
        Set<int[]>currentOffsets = prepareOffsets(xPos, yPos);
        boolean hasLiberty = false;
        for (int[] offset : currentOffsets) {
            hasLiberty |= this.grid[xPos + offset[0]][yPos + offset[1]].isCleared();
        }
        return hasLiberty && this.wagered.isCleared();
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
