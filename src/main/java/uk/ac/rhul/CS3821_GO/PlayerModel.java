package uk.ac.rhul.CS3821_GO;
import java.lang.IllegalArgumentException;
import java.util.*;

public class PlayerModel {
    private final StoneTypes type;
    private final StoneGroups stoneGroups = new StoneGroups();

    PlayerModel(StoneTypes type){
        if(type == StoneTypes.NONE){
            throw new IllegalArgumentException("Player must either use white or black stones.");
        }
        this.type = type;
        this.stoneGroups.stones = new HashMap<>();
        this.stoneGroups.stonesInverse = new HashMap<>();
    }
    public StoneTypes getType(){
        return this.type;
    }

    public StoneGroups getGroup() {
        return this.stoneGroups;
    }

}
