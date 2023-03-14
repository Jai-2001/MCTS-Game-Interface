package uk.ac.rhul.CS3821_GO;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class MCTSNode {

    private MCTSNode parent;
    private ConcurrentHashMap<Integer, MCTSNode> children;
    private byte[] move;

    private LinkedList<byte[]> moveList;
    private final EndStates endState;
    private double visits;
    private double score;

    MCTSNode() {
        this(EndStates.RUNNING,null);
    }


    MCTSNode(EndStates state, Map<Integer,MCTSNode> children){
        if (children != null) {
            this.children = new ConcurrentHashMap<>(children);
        } else{
            this.children =new ConcurrentHashMap<>();
        }
        this.endState = state;
        this.move = null;
        this.visits = 0;
        this.score = 0;
    }

    public void add(MCTSNode child){
        this.children.put(child.hashCode(), child);
    }

    private static final byte[] PASS = new byte[]{-1,-1};
    public List<byte[]> buildMoveList(){
        if(moveList==null){
            LinkedList<byte[]> newMoves = new LinkedList<>();
            MCTSNode notRoot = this;
            while (notRoot.getMove()!=null){
                newMoves.addFirst(notRoot.getMove());
                notRoot = notRoot.getParent();
            }
            this.moveList= newMoves;
        }

        return Collections.unmodifiableList(moveList);
    }
    public Map<Integer,MCTSNode> getChildren(){
        return (this.children);
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
    @Override
    public String toString(){
        return MessageFormat.format("[({0} -> {1}) = {2}] Score: {3}, Visits: {4}, Children: {5}",
                Arrays.toString(Optional.ofNullable(parent).orElse(this).move),
                Arrays.toString(this.move),
                this.endState.toString(),
                score,
                visits,
                Optional.ofNullable(this.children).orElse(EMPTY).size()
        ).replace("[-1, -1]", "PASS");
    }

    private static final ConcurrentHashMap<Integer, MCTSNode> EMPTY = new ConcurrentHashMap<>();
    public boolean moveAlreadyDone(byte[] move){
        return this.children.containsKey(getHashForMove(move));
    }


    @Override
    public int hashCode(){
        return getHashForMove(this.move);
    }

    public static int getHashForMove(byte[] move){
        move = Optional.ofNullable(move).orElse(new byte[]{-8,-8});
        return  200 * move[0] + 2 * move[1];
    }

    public MCTSNode bestLeaf(){
        MCTSNode leaf = this;
        while (!leaf.getChildren().isEmpty()){
            leaf = leaf.getChildren().values().stream().max(Comparator.comparing(MCTSNode::getVisits)).get();
        }
        return leaf;
    }
}
