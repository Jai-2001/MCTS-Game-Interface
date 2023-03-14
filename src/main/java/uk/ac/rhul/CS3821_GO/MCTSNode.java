package uk.ac.rhul.CS3821_GO;

import java.util.ArrayList;

public class MCTSNode {

    private MCTSNode parent;
    private ArrayList<MCTSNode> children;
    private ArrayList<int[]> moveList;
    private final EndStates endState;
    private double visits;
    private double score;

    MCTSNode() {
        this.children = null;
        this.moveList = null;
        this.endState = EndStates.RUNNING;
        this.visits = 0;
        this.score = 0;
    }

    MCTSNode(EndStates state, ArrayList<MCTSNode> children){
        this.children = children;
        this.moveList = null;
        this.endState = state;
        this.visits = 0;
        this.score = 0;
    }

    public void add(MCTSNode child){
            if(this.children == null){
                this.children = new ArrayList<>();
            }
        this.children.add(child);
    }

    public ArrayList<MCTSNode> getChildren(){
        return this.children == null ? new ArrayList<>() : this.children;
    }

    public void setMove(byte[] moves){
        this.move = moves;
    }

    public byte[] getMove(){
        return this.move;
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

    public MCTSNode getParent() {
        return parent;
    }

    public void setParent(MCTSNode parent) {
        this.parent = parent;
    }
}
