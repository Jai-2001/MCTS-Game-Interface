package uk.ac.rhul.CS3821_GO;


import java.util.Arrays;
import java.util.Set;

public class StoneMap {

    private final Intersection[][] grid;
    private Intersection wagered;

    StoneMap(byte xSize, byte ySize){
        this.grid = new Intersection[xSize][ySize];
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                this.grid[x][y] = new Intersection((byte) x, (byte) y);
            }
        }
        this.wagered = null;
    }

    public boolean checkMove(int xPos, int yPos, TurnState turn) {
        this.wagered = this.grid[xPos][yPos];
        int[][] currentOffsets = OffsetFactory.prepareOffsets(xPos, yPos);
        boolean hasLiberty = false;
        for (int[] offset : currentOffsets) {
            hasLiberty |= this.grid[xPos + offset[0]][yPos + offset[1]].isCleared();
        }
        boolean captureOccurred = checkCapture(xPos,yPos, turn);
        return (hasLiberty || captureOccurred) && this.wagered.isCleared();
    }

    public boolean checkCapture(int xPos, int yPos, TurnState turn){
        StoneGroups friendly = turn.getCurrentPlayer().getGroups();
        StoneGroups hostile = turn.getPreviousPlayer().getGroups();
        int playerIndex = turn.getCurrentPlayer().getType().ordinal();
        Intersection relevant = this.grid[xPos][yPos];
        for(int[] offset : OffsetFactory.prepareOffsets(xPos, yPos)){
            Intersection adjacent = this.grid[xPos + offset[0]][yPos + offset[1]];
            if(!adjacent.isCleared()){
                if(adjacent.getRepresentation() == playerIndex){
                    int neighbour = friendly.getGroup(adjacent);
                    Set<Intersection> neighbourGroup = friendly.getLiberties(neighbour);
                    if(neighbourGroup != null) {
                        if (friendly.getLiberties(neighbour).size() > 1) {
                            return true;
                        } else if(friendly.getAllGroups().stream().filter(i -> friendly.getLiberties(i) != null)
                                .allMatch(i -> friendly.getLiberties(i).size() <= 1)){
                            return true;
                        }
                    }
                } else{
                    int enemy = hostile.getGroup(adjacent);
                    Set<Intersection> eyes = hostile.getLiberties(enemy);
                    if(eyes != null)  {
                        if (eyes.size() == 1) {
                            Intersection last = eyes.iterator().next();
                            if(relevant.equals(last)){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    public Intersection getStone(int xPos, int yPos){
        return this.grid[xPos][yPos];
    }

    public Intersection[][] copy(){
        return grid;
    }

    public Intersection getWagered() {
        return wagered;
    }
}
