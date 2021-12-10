package uk.ac.rhul.CS3821_GO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StoneGroups {
    Map<Integer, Set<Intersection>> stones;
    Map<Intersection, Integer> stonesInverse;
    Map<Integer, Set<Intersection>> liberties;

    public StoneGroups() {
        this.stones = new HashMap<>();
        this.stonesInverse = new HashMap<>();
        this.liberties = new HashMap<>();
    }

    public int getGroup(Intersection query) {
        if (!stonesInverse.containsKey(query)) {
            throw new IllegalArgumentException("This stone does not belong to this player.");
        }
        return stonesInverse.get(query);
    }

    public void addStone(int i, Intersection stone) {
        Set<Intersection> groupSet;
        if (!stones.containsKey(i)) {
            groupSet = new HashSet<>();
            stones.put(i, groupSet);
        } else {
            groupSet = stones.get(i);
        }
        groupSet.add(stone);
        stonesInverse.putIfAbsent(stone, i);
    }

    public void combineGroups(int parent, int child){
        Set<Intersection> parentSet = stones.get(parent);
        Set<Intersection> childSet = stones.get(child);
        parentSet.addAll(childSet);
        stonesInverse.replaceAll((stone, group) -> group == child ? parent : group);
        stones.remove(child);
        if(liberties.containsKey(parent) && liberties.containsKey(child)){
            liberties.get(parent).addAll(liberties.get(child));
            liberties.get(child).clear();
        }
    }

    public void clearGroup(int i) {
        Set<Intersection> clearSet = stones.remove(i);
        for (Intersection toRemove : clearSet) {
            toRemove.clear();
            stonesInverse.remove(toRemove);
        }
        if(liberties.containsKey(i)){
            liberties.get(i).clear();
        }
        stones.remove(i);
    }

    public Intersection[] getGroupStones(int i) {
        Set<Intersection> groupSet = stones.get(i);
        return groupSet.toArray(new Intersection[0]);
    }

    public void addLiberty(int i, Intersection intersection) {
        Set<Intersection> eyes;
        if(liberties.containsKey(i)){
            eyes = this.liberties.get(i);
        } else {
            eyes = new HashSet<>();
            liberties.put(i, eyes);
        }
        eyes.add(intersection);
    }

    public Set<Intersection> getLiberties(int i) {
        return this.liberties.get(i);
    }

    public void clearLiberties(){
        for (int forStone: liberties.keySet()) {
            liberties.get(forStone).clear();
        }
    }

    public Set<Integer> getAllGroups(){
        return stones.keySet();
    }
}