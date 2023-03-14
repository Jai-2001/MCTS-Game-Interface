package uk.ac.rhul.CS3821_GO;

import com.kitfox.svg.A;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static uk.ac.rhul.CS3821_GO.EndStates.*;

public class MonteCarloTreeSearch {
    private final double explorationConfidence;
    private final int searchDepth;
    public final GameMCTSInterface gameInterface;
    private MCTSNode shiftingRoot;
    private final Random rng;
    private final int rollOuts;
    private final int iterations;
    public ArrayList<byte[]> moveList;

    private int backgroundIterations;
    private int maxBackground;
    public MonteCarloTreeSearch(int scoreLimit, boolean isBlack){
        this(scoreLimit, Math.sqrt(2.0), 450, 35, new Random(), isBlack, 350);
    }

    public MonteCarloTreeSearch(int limit, double confidence, int depth, int rollouts, Random rng, boolean isBlack, int iterations) {
        this(limit,confidence,depth,rollouts,rng,isBlack,iterations,0);
    }
    public MonteCarloTreeSearch(int limit, double confidence, int depth, int rollouts, Random rng, boolean isBlack, int iterations, int background) {
        this.explorationConfidence = confidence;
        this.searchDepth = depth;
        this.rng = rng;
        this.gameInterface = new GoMCTSInterface(isBlack, limit, rng);
        this.rollOuts = rollouts;
        this.iterations = iterations;
        this.shiftingRoot = new MCTSNode();
        this.moveList = new ArrayList<>();
        this.maxBackground = background;
        this.backgroundIterations = 0;
    }

    public void moveTaken(byte[] taken){
        //System.out.println("USER HAS TAKEN MOVE -> "+ Arrays.toString(taken));
        MCTSNode candidate;
        this.moveList.add(taken);
        HashSet<MCTSNode> falsePredictions = new HashSet<>();
        for (MCTSNode i : this.shiftingRoot.getChildren().values()) {
            if (!Arrays.equals(i.getMove(),taken)) {
                falsePredictions.add(i);
            }

        }
        this.shiftingRoot.getChildren().values().removeAll(falsePredictions);
        candidate = this.shiftingRoot.getChildren().isEmpty() ? shiftingRoot : select(shiftingRoot);
        if (candidate ==null || candidate == this.shiftingRoot) {
            candidate = new MCTSNode();
            candidate.setMove(taken);
            this.shiftingRoot.add(candidate);
            candidate.setParent(this.shiftingRoot);
        }

        this.shiftingRoot = candidate;
    }
    public byte[] path(){
        int iterations = this.iterations;
        if(maxBackground<Integer.MAX_VALUE && backgroundIterations<maxBackground){
            iterations+=(maxBackground-backgroundIterations);
        }
        MCTSNode best = path(iterations);
        GameModel currentModel = gameInterface.presimulate(shiftingRoot.buildMoveList());
        if(best==null) {
            return gameInterface.randomMove(currentModel);
        } else {
            return best.getMove();
        }
    }

    public MCTSNode path(int iterations){
        System.out.println("Pathing: " + iterations);
        iterate(iterations);
        return select();
    }

    public MCTSNode backgroundPath(int iterations){
        if(incrementIterations(iterations)){
            iterate(iterations);
        }
        return select();
    }

    private void iterate(int iterations){
        Stream.generate(this::explore).limit(iterations).parallel().forEach((leaf)->
                IntStream.range(0, rollOuts)
                        .parallel()
                        .forEach(n->this.rollOut(leaf))
        );
    }

    public MCTSNode explore(){
        return UCB(this.shiftingRoot);
    }
    private MCTSNode getParentSafe(MCTSNode leaf) {
        return leaf.getParent() == null ? this.shiftingRoot : leaf.getParent();
    }

    public MCTSNode UCB(MCTSNode current){
        if(current==null){
            return shiftingRoot;
        } else {
            ArrayList<MCTSNode> children = new ArrayList<>(current.getChildren().values());
            if(children.isEmpty()){
                return current;
            }
            return UCB(current, children);
        }
    }
    public MCTSNode UCB(MCTSNode current, ArrayList<MCTSNode> children) {
        Collections.shuffle(children);
        MCTSNode bestNode = children.get(0);
        double bestScore = Double.NEGATIVE_INFINITY;
            for (MCTSNode child : children) {
                    double score = this.explorationConfidence;
                    if(child.getVisits() <= 1){
                        score -=1;
                    } else {
                        score *= Math.sqrt(Math.log(current.getVisits())/child.getVisits());

                    }
                    score += child.getScore();
                    if (score >= bestScore) {
                        bestScore = score;
                        bestNode = child;
                    }
            }
        //bestNode.incrementVisits();
        return bestNode;
    }

    public MCTSNode select(){
        return select(shiftingRoot);
    }
    public MCTSNode select(MCTSNode initial) {
        if (initial==null) {
            iterate(1);
            return select();
        };
        ArrayList<MCTSNode> children = new ArrayList<>(initial.getChildren().values());
        Collections.shuffle(children, this.rng);
        Set<MCTSNode> risky = new HashSet<>();
            for (MCTSNode promising: children) {
                if(promising.getEndState() == WON) {
                    return promising;
                } else if(promising.getChildren().values().stream().anyMatch((i)-> i.getEndState() == LOST)){
                    risky.add(promising);
                }
            }
            if(!risky.isEmpty()){
                if(risky.containsAll(children)) {
                    System.err.println("DANGER");
                    rollOut(initial);
                    return select(initial);

                } else {
                    children.removeAll(risky);
                }
            }
        return children.stream()
                .max(Comparator.comparing(MCTSNode::getScore))
                .orElse(null);
    }
    private MCTSNode rollOut(MCTSNode start) {
        ArrayList<byte[]> subMoves = new ArrayList<>(start.buildMoveList());
        GameModel simModel = this.gameInterface.presimulate(subMoves, 0);
        int depth = searchDepth;
        MCTSNode ball = start;
            top: while (ball.getEndState() == RUNNING && depth >= 0){
                byte[] newMove;
                boolean hasBeenDone;
                int tries = 0;
                    do {
                        newMove = gameInterface.randomMove(simModel);
                        hasBeenDone = ball.moveAlreadyDone(newMove);
                        if(hasBeenDone){
                            tries++;
                            if(tries > this.rollOuts){
//                                MCTSNode cannon = ball;
//                                while(cannon != start){
//                                    cannon = cannon.getParent();
//                                    if (cannon != null) cannon.getChildren().values().removeIf((i)->true);
//                                }
                                return start;
//                                continue top;
                            }

                        }

                    } while (hasBeenDone);
            simModel.nextTurn();
            MCTSNode nextBall = new MCTSNode(gameInterface.someoneWon(simModel), null);
            nextBall.setMove(newMove);
            nextBall.setParent(ball);
            ball.add(nextBall);
            ball = nextBall;
            depth--;
        }
        backpropagate(ball);
        return ball;
    }

    private void backpropagate(MCTSNode ball) {
        MCTSNode start = shiftingRoot;
            if(ball.getEndState()!= RUNNING) {
                double score = switch (ball.getEndState()) {
                    case WON -> 1;
                    case LOST -> -1;
                    default -> 0;
                };
                start.setScore(start.getScore() + score);
                while (ball != start) {
                    ball.incrementVisits();
                    ball.setScore(ball.getScore() + score);
                    ball = ball.getParent();
                }
                ball.incrementVisits();
            }
    }

    public MCTSNode getShiftingRoot(){
        return this.shiftingRoot;
    }
    public EndStates someoneWon(GoModel model){
        return gameInterface.someoneWon(model);
    }
    public int checkIterations() {
        return this.backgroundIterations;
    }
    public boolean incrementIterations(int requested) {
        if(maxBackground>requested+backgroundIterations+1){
            this.backgroundIterations+=requested;
            return true;
        }
        return false;
    }

    public boolean clearIterations() {
        boolean wasComplete = this.backgroundIterations>=this.maxBackground;
        this.backgroundIterations = 0;
        return wasComplete;
    }
}