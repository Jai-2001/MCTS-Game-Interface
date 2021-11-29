package uk.ac.rhul.CS3821_GO;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

    public void addStone(int i, Intersection stone) {
        strings.put(stone, i);
    }

    public void combineGroups(int parent, int child) {
        strings.replaceAll((stone,group) -> group == child ? parent : group);
    }

    public void clearGroup(int toClear) {
        Iterator<Map.Entry<Intersection, Integer>> mutable = strings.entrySet().iterator();
        mutable.forEachRemaining((Map.Entry<Intersection, Integer> entry) -> {
                int group = entry.getValue();
                    if (group == toClear){
                        entry.getKey().clear();
                        mutable.remove();
                    }
            }
        );
    }

    public List<Intersection> getGroupStones(int i) {
        return new ArrayList<Intersection>();
    }
}
