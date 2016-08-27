package dr.games.mspacman.agents;

import dr.games.mspacman.model.Direction;
import dr.games.mspacman.model.PacAgent;
import dr.games.mspacman.util.PacUtil;
import dr.games.mspacman.model.GameState;
import dr.games.mspacman.model.Node;

public class PillEater implements PacAgent {

    @Override
    public Direction direction(GameState gs)
    {
        Node pillNode = null;
        int bestDist = Integer.MAX_VALUE;

        // for every pill node container
        for (Node n : gs.getMazeState().getMaze().getPillsNodes())
        {
            // is there a pill in the node
            if (gs.getMazeState().getPillsBitSet().get(n.getPillIndex()))
            {
                // if the distance from pacman to the node is shorter than the previous best
                if (gs.getMazeState().getMaze().distance(gs.getPacman().getCurrentNode(), n) < bestDist) {
                    pillNode = n;
                    bestDist = gs.getMazeState().getMaze().distance(gs.getPacman().getCurrentNode(), n);
                }
            }
        }

        Direction bestDir = null;
        int bestDirValue = Integer.MAX_VALUE;

        for (Direction dir : gs.getPacmanMoves())
        {
            // next position if moving on this direction (dir)
            Node nextNode = PacUtil.nextNode(gs.getPacman().getCurrentNode(), dir);
            
            // no turn back
            if (dir != dir.opposite())
            {
                // if this move leads to a shortest paths
                if (gs.getMazeState().getMaze().distance(nextNode, pillNode) < bestDirValue) {
                    bestDir = dir;
                    bestDirValue = gs.getMazeState().getMaze().distance(nextNode, pillNode);
                }
            }
        }

        return bestDir;
    }

    @Override
    public String toString() {
        return "PillEater";
    }
}
