package uk.ac.rhul.CS3821_GO;
import java.lang.IllegalArgumentException;

public class PlayerModel implements Cloneable{
    private final StoneTypes type;
    private final StoneGroups stoneGroups;
    private int concededPoints;

    PlayerModel(StoneTypes type){
        if(type == StoneTypes.NONE){
            throw new IllegalArgumentException("Player must either use white or black stones.");
        }
        this.type = type;
        this.stoneGroups = new StoneGroups();
        this.concededPoints = 0;
    }
    public StoneTypes getType(){
        return this.type;
    }

    public StoneGroups getGroups() {
        return this.stoneGroups;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof PlayerModel){
            return this.getType() == ((PlayerModel) o).getType();
        }
        return false;
    }

    public void incrementConcededPoints(int amount){
        this.concededPoints+= amount;
    }

    public int getConcededPoints() {
        return this.concededPoints;
    }
}
