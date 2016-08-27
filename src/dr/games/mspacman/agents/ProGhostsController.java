package dr.games.mspacman.agents;

import dr.games.mspacman.model.*;
import dr.games.mspacman.util.PacUtil;

import java.util.List;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

import static dr.games.mspacman.model.GhostType.BLINKY;

public class ProGhostsController implements GhostsAgent {

    private static final Random rand = new Random();
    private double blinkProb;
    private double pinkyProb;
    private double inkyProb;
    private double sueProb;

    public ProGhostsController(double blinkProb, double pinkyProb, double inkyProb, double sueProb) {
        this.blinkProb = blinkProb;
        this.pinkyProb = pinkyProb;
        this.inkyProb = inkyProb;
        this.sueProb = sueProb;
    }

    @Override
    public Map<Ghost, Direction> direction(GameState gs)
    {
        Map<Ghost, Direction> map = new HashMap<Ghost, Direction>();

        for (Ghost ghost : gs.getGhosts())
        {
            double prob;

            if (ghost.getType() == BLINKY)
                prob = blinkProb;
            else if (ghost.getType() == GhostType.PINKY)
                prob = pinkyProb;
            else if (ghost.getType() == GhostType.INKY)
                prob = inkyProb;
            else if (ghost.getType() == GhostType.SUE)
                prob = sueProb;
            else
                prob = 1.00;

            map.put(ghost, ghostDir(gs, ghost, gs.getGhostsMoves().get(ghost), prob));
        }

        return map;
    }

    public Direction ghostDir(GameState gs, Ghost ghost, List<Direction> dirs, double prob)
    {
        // move on the shortest path to pacman
        if (ghost.getCurrentNode().getNeighbors().size() >= 3 && rand.nextDouble() < prob)
        {
            Node bestMove = null;

            for (Node n : ghost.getCurrentNode().getNeighbors())
            {
                if (PacUtil.directionToNeighbor(ghost.getCurrentNode(), n) != ghost.getDirection().opposite())
                {
                    if (bestMove == null || gs.getMazeState().getMaze().distance(n, gs.getPacman().getCurrentNode())
                            < gs.getMazeState().getMaze().distance(bestMove, gs.getPacman().getCurrentNode())) {
                        bestMove = n;
                    }
                }
            }

            return PacUtil.directionToNeighbor(ghost.getCurrentNode(), bestMove);
        }

        return PacUtil.direction(ghost, dirs);
    }



}