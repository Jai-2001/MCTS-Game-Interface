package uk.ac.rhul.CS3821_GO;
import java.lang.IllegalArgumentException;

public class PlayerModel {
    private StoneTypes type;
    private StoneGroups stoneGroups;

    PlayerModel(StoneTypes type){
        if(type == StoneTypes.NONE){
            throw new IllegalArgumentException("Player must either use white or black stones.");
        }
        this.type = type;
        this.stoneGroups = new StoneGroups();
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

}
