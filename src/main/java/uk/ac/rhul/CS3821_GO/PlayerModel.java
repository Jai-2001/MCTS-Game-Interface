package uk.ac.rhul.CS3821_GO;
import java.lang.IllegalArgumentException;
import java.util.*;

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

    public StoneGroups getGroup() {
        return this.stoneGroups;
    }

}
