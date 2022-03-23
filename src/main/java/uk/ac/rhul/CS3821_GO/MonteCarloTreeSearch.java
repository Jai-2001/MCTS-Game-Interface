package uk.ac.rhul.CS3821_GO;

import java.util.*;

import static uk.ac.rhul.CS3821_GO.EndStates.*;

public class MonteCarloTreeSearch {
    private final double explorationConfidence;
    private final int searchDepth;
    private final GameMCTSInterface gameInterface;
    private MCTSNode shiftingRoot;
    private final Random rng;
    private final int rollOuts;
    private final int iterations;
    private ArrayList<int[]> moveList;

    public MonteCarloTreeSearch(int scoreLimit, boolean isBlack){
        this(scoreLimit, 50000, 20, 81, new Random(), isBlack, 5);
    }

    public MonteCarloTreeSearch(int scoreLimit, double confidence, int depth, int rollouts, Random rng, boolean isBlack, int iterations) {
        this.explorationConfidence = confidence;
        this.searchDepth = depth;
        this.rng = rng;
        this.gameInterface = new GoMCTSInterface(isBlack, scoreLimit, rng);
        this.rollOuts = rollouts;
        this.iterations = (iterations/scoreLimit) + 1;
        this.shiftingRoot = new MCTSNode();
        this.moveList = new ArrayList<>();
    }

    public void moveTaken(int[] taken){
        MCTSNode candidate;
        this.moveList.add(taken);
        HashSet<MCTSNode> falsePredictions = new HashSet<>();
        ArrayList<int[]> moves = new ArrayList<>(this.moveList);
        next:
        for (MCTSNode i : this.shiftingRoot.getChildren()) {
            List<int[]> subMoves = i.getMoves().subList(0, moves.size());
            for (int j = 0; j < subMoves.size(); j++) {
                if (subMoves.get(j)[0] != moves.get(j)[0] || subMoves.get(j)[1] != moves.get(j)[1]) {
                    falsePredictions.add(i);
                    continue next;
                }
            }
        }
        this.shiftingRoot.getChildren().removeAll(falsePredictions);
        candidate = select(this.shiftingRoot);
        if (candidate == this.shiftingRoot) {
            candidate = new MCTSNode();
            candidate.setMoves(moves);
            this.shiftingRoot.add(candidate);
        }
        candidate.setParent(null);
        this.shiftingRoot = candidate;
    }

    public int[] path(){
        MCTSNode leaf;
            for (int i = 0; i < iterations; i++) {
                leaf = UCB(this.shiftingRoot);
                for (int j = 0; j < this.rollOuts; j++) {
                    GameModel simModel = this.gameInterface.presimulate(leaf.getMoves(), 0);
                    MCTSNode terminates = rollOut(leaf, simModel);
                    if (terminates != leaf){
                        MCTSNode parent = getParentSafe(leaf);
                        backpropagate(parent, terminates);
                    }
                }
            }
        leaf = select(this.shiftingRoot);
        int[] bestMove =  leaf.getMoves().get(this.moveList.size());
        return bestMove;
    }

    private MCTSNode getParentSafe(MCTSNode leaf) {
        return leaf.getParent() == null ? this.shiftingRoot : leaf.getParent();
    }

    public MCTSNode UCB(MCTSNode current) {
        double bestScore = Integer.MIN_VALUE;
        MCTSNode bestNode = current;
        ArrayList<MCTSNode> children = current.getChildren();
        Collections.shuffle(children);
            for (MCTSNode child : children) {
                double score = child.getScore();
                double ratio = Math.log(current.getVisits())/child.getVisits();
                score += (this.explorationConfidence * Math.sqrt(ratio));

                    if (score >= bestScore) {
                        bestScore = score;
                        bestNode = child;
                    }
            }
        return bestNode;
    }

    protected MCTSNode select(MCTSNode initial) {
        Collections.shuffle(initial.getChildren(), this.rng);
        int[] preventativeMove = null;
            for (MCTSNode promising: initial.getChildren()) {
                if(preventativeMove != null){
                    if (Arrays.equals(preventativeMove, promising.getMoves().get(initial.getMoves().size()))){
                        return promising;
                    }
                } else if(promising.getEndState() == WON) {
                    return promising;
                }
                Optional<MCTSNode> losingCapture = promising.getChildren().stream().filter((i) -> i.getEndState()== LOST).findFirst();
                if (losingCapture.isPresent()){
                    preventativeMove = losingCapture.get().getMoves().get(initial.getMoves().size()+1);
                }
            }
            if (preventativeMove!=null){
                MCTSNode prescient = new MCTSNode(RUNNING, null);
                ArrayList<int[]> prescientMoves = new ArrayList<>(initial.getMoves());
                prescientMoves.add(preventativeMove);
                prescient.setMoves(prescientMoves);
                prescient.setParent(initial);
                return prescient;
            }
        return UCB(initial);
    }

    private MCTSNode rollOut(MCTSNode start, GameModel simModel) {
        ArrayList<int[]> runningMoves = start.getMoves();
        MCTSNode ball = start;
        int depth = this.searchDepth;
            while (ball.getEndState() == RUNNING && depth >= 0){
                ArrayList<int[]> uniqueMoves = new ArrayList<>();
                    for (MCTSNode uniqueChild: ball.getChildren()) {
                        uniqueMoves.add(uniqueChild.getMoves().get(ball.getMoves().size()));
                    }
                int[] newMove;
                boolean hasBeenDone;
                int tries = 0;
                    do {
                        hasBeenDone = false;
                        newMove = gameInterface.randomMove(simModel);
                            for (int[] unique : uniqueMoves) {
                                if (unique[0] == newMove[0] && unique[1] == newMove[1]) {
                                    hasBeenDone = true;
                                    break;
                                }
                            }
                        tries++;
                        if(tries > this.rollOuts){
                            MCTSNode cannon = ball;
                            while(cannon != start){
                                cannon = cannon.getParent();
                                if (cannon != null) cannon.getChildren().removeIf((i)->true);
                            }
                            return start;
                        }
                    } while (hasBeenDone);
            runningMoves = new ArrayList<>(runningMoves);
            runningMoves.add(newMove);
            simModel.nextTurn();
            MCTSNode nextBall = new MCTSNode(gameInterface.someoneWon(simModel), null);
            nextBall.setMoves(runningMoves);
            nextBall.setParent(ball);
            ball.add(nextBall);
            ball = nextBall;
            depth--;
        }
        return ball;
    }

    private void backpropagate(MCTSNode start, MCTSNode ball) {
        start.incrementVisits();
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
            }
    }

    public EndStates someoneWon(GoModel model){
        return gameInterface.someoneWon(model);
    }
}