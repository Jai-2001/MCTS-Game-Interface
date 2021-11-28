package uk.ac.rhul.CS3821_GO;
import java.lang.IllegalArgumentException;
import java.util.HashMap;
import java.util.Map;

public class PlayerModel {
    private StoneTypes type;
    private Map<Integer, Intersection> strings;
    PlayerModel(StoneTypes type){
        if(type == StoneTypes.NONE){
            throw new IllegalArgumentException("Player must either use white or black stones.");
        }
        this.type = type;
        this.strings = new HashMap<Integer,Intersection>();
    }
    public StoneTypes getType(){
        return this.type;
    }

    public int getKey(Intersection stone) {
        if (!strings.containsValue(stone)){
            throw new IllegalArgumentException("This stone does not belong to this player.");
        }
        return 0;
    }

    public void addStone(int i, Intersection unique) {
    }
}
