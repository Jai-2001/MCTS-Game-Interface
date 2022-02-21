package uk.ac.rhul.CS3821_GO;

import java.util.ArrayList;

public class GoNode{

    private ArrayList<GoNode> children;

    GoNode() {
        this.children = null;
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
}
