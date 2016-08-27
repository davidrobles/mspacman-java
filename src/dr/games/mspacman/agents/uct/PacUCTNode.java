package dr.games.mspacman.agents.uct;

import dr.games.mspacman.agents.ProGhostsController;
import dr.games.mspacman.model.Direction;
import dr.games.mspacman.model.GameState;
import dr.games.mspacman.model.GhostType;
import dr.games.mspacman.util.PacUtil;

import java.util.*;

public class PacUCTNode {


    int depth = 0;
    
    // total visits to this node during a rollout
    private int visits = 0;

    double nv = 0;

    // the total value accumulated by this node during the rollout
    private double value = 0;
    
    private GameState gameState;

    List<PacUCTNode> nextStates = new ArrayList<PacUCTNode>();

    private Direction dir;

    private static final Random rand = new Random();

    private static final ProGhostsController ghostsController = new ProGhostsController(100, 100, 100, 100);

    public PacUCTNode(GameState gameState, Direction dir, int depth)
    {
        this.gameState = gameState;
        this.dir = dir;
        this.depth = depth;

        if (!gameState.isGameOver() && depth < PacUCT.MAX_DEPTH)
            generateNextStates();
    }

    public GameState getGameState() {
        return gameState;
    }

    public Direction getDir() {
        return dir;
    }

    public List<PacUCTNode> getNextGameStates() {
        return nextStates;
    }

    public int getVisits() {
        return visits;
    }

    public double getValue() {
        return value;
    }

    public void update(double value) {
        assert value >= 0.0 && value <= 1.0;
        this.value += value;
        visits++;
    }

    public int getDepth() {
        return depth;
    }

    private void generateNextStates()
    {
        List<Direction> possibleDirs = gameState.getPacmanMoves();

        // remove the move that takes pacman to the previous node ONLY if this is not the root node 
        if (depth > 0) {
            possibleDirs.remove(gameState.getPacman().getDirection().opposite());
        }

        for (Direction dir : possibleDirs) {
            GameState playGS = gameState.copy();
//            playGS.nextState(dir, PacUtil.ghostNeutralMoves(playGS.getGhosts()));
            playGS.nextState(dir, ghostsController.direction(playGS));
            nextStates.add(new PacUCTNode(playGS, dir, ++depth));
        }
    }

    @Override
    public String toString() {
        return "" + visits;
    }

}
