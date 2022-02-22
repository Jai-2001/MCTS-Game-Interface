package uk.ac.rhul.CS3821_GO;

import java.util.ArrayList;

public class GoNode{

    private ArrayList<GoNode> children;
    private ArrayList<int[]> moveList;

    GoNode() {
        this.children = null;
        this.moveList = null;
    }

    public void add(GoNode child){
            if(this.children == null){
                this.children = new ArrayList<>();
            }
        this.children.add(child);
    }

    public ArrayList<GoNode> getChildren(){
        return this.children;
    }

    public void setMoves(ArrayList<int[]> moves){
        this.moveList = moves;
    }

    public ArrayList<int[]> getMoves(){
        return this.moveList;
    }
}
