package uk.ac.rhul.CS3821_GO;
import java.lang.IllegalArgumentException;
import java.util.HashMap;
import java.util.Map;

public class PlayerModel {
    private final StoneTypes type;
    private final Map<Intersection, Integer> strings;
    PlayerModel(StoneTypes type){
        if(type == StoneTypes.NONE){
            throw new IllegalArgumentException("Player must either use white or black stones.");
        }
        this.type = type;
        this.strings = new HashMap<>();
    }
    public StoneTypes getType(){
        return this.type;
    }

    public int getGroup(Intersection stone) {
        if (!strings.containsKey(stone)){
            throw new IllegalArgumentException("This stone does not belong to this player.");
        }
        return strings.get(stone);
    }

    public void addStone(int i, Intersection unique) {
        strings.put(unique, i);
    }
}
