package uk.ac.rhul.CS3821_GO;

import java.util.ArrayList;
import java.util.List;

public class GoNode{

    private ArrayList<GoNode> children;
    private ArrayList<int[]> moveList;
    private EndStates endState;
    private int visits;
    private double score;

    GoNode() {
        this.children = null;
        this.moveList = null;
        this.endState = EndStates.RUNNING;
        this.visits = 0;
    }

    GoNode(EndStates state, GoNode[] children){
        this.children = children == null ? null : new ArrayList<>(List.of(children));
        this.moveList = null;
        this.endState = state;
        this.visits = 0;
    }

    public void add(GoNode child){
            if(this.children == null){
                this.children = new ArrayList<>();
            }
        this.children.add(child);
    }

    public ArrayList<GoNode> getChildren(){
        return this.children == null ? new ArrayList<>() : this.children;
    }

    public void setMoves(ArrayList<int[]> moves){
        this.moveList = moves;
    }

    public ArrayList<int[]> getMoves(){
        return this.moveList;
    }

    public void setEndState(EndStates state) {
        this.endState = state;
    }

    public EndStates getEndState() {
        return this.endState;
    }

    public void incrementVisits(){
        this.visits++;
    }

    public double getVisits() {
        return this.visits;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
