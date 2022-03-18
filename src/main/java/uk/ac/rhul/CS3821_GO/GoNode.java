package uk.ac.rhul.CS3821_GO;

import java.util.ArrayList;

public class GoNode{

    private GoNode parent;
    private ArrayList<GoNode> children;
    private ArrayList<int[]> moveList;
    private EndStates endState;
    private double visits;
    private double score;

    GoNode() {
        this.children = null;
        this.moveList = null;
        this.endState = EndStates.RUNNING;
        this.visits = 0;
        this.score = 0;
    }

    GoNode(EndStates state, ArrayList<GoNode> children){
        this.children = children;
        this.moveList = null;
        this.endState = state;
        this.visits = 0;
        this.score = 0;
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
        return this.moveList == null ? new ArrayList<>(): this.moveList;
    }

    public EndStates getEndState() {
        return this.endState;
    }

    public void incrementVisits(){
        this.visits = this.visits + 1.0;
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

    public GoNode getParent() {
        return parent;
    }

    public void setParent(GoNode parent) {
        this.parent = parent;
    }
}
