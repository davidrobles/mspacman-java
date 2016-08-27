package dr.games.mspacman.agents;

import dr.games.mspacman.model.*;
import dr.games.mspacman.util.PacUtil;
import dr.games.mspacman.view.AgentView;

import java.awt.*;
import java.util.*;
import java.util.List;

public class RealGhosts implements GhostsAgent, AgentView {

    private GameState gameState;
    private static final Random rand = new Random();

    private Direction pinkyDir(Ghost ghost)
    {
        if (gameState.getMazeState().getMaze().distance(ghost.getCurrentNode(),
                gameState.getPacman().getCurrentNode()) > 50)
            return blinkyDir(ghost);



        return Direction.NEUTRAL;
    }

    private Direction inkyDir(Ghost inky, Ghost blinky)
    {
        // When green is very close to red. Green takes on
        // a character nearly identical to red.
        if (gameState.getMazeState().getMaze().distance(inky.getCurrentNode(), blinky.getCurrentNode()) < 30) {
            return blinkyDir(inky);
        }

        // When green is distant from red she becomes unique in her
        // actions.Just as red has a priority order so has green.
        // Hers, however is quite different.
        if (gameState.getGhostsMoves().get(inky).contains(Direction.RIGHT))
            return Direction.RIGHT;
        // go left
        if (gameState.getGhostsMoves().get(inky).contains(Direction.LEFT))
            return Direction.LEFT;
        // go down
        if (gameState.getGhostsMoves().get(inky).contains(Direction.DOWN))
            return Direction.DOWN;
        // go right
        if (gameState.getGhostsMoves().get(inky).contains(Direction.UP))
            return Direction.UP;

        return Direction.NEUTRAL;
    }

    private Direction sueDir(Ghost sue) {
        int randIndex = rand.nextInt((gameState.getGhostsMoves().get(sue)).size());
        return gameState.getGhostsMoves().get(sue).get(randIndex);
    }

    private Direction blinkyDir(Ghost ghost)
    {
        // If there is only one possible move, return it
        if (gameState.getGhostsMoves().get(ghost).size() == 1) {
            return gameState.getGhostsMoves().get(ghost).get(0);
        }
        // Red always tries to shorten either the horizontal or vertical
        // distance between him-self and Ms. Pac-Man, whichever is longer.
        else {
            int ghostX = ghost.getCurrentNode().getX();
            int ghostY = ghost.getCurrentNode().getY();
            int pacmanX = gameState.getPacman().getCurrentNode().getX();
            int pacmanY = gameState.getPacman().getCurrentNode().getY();

            int horDist = ghostX - pacmanX;
            int verDist = ghostY - pacmanY;

            if (Math.abs(horDist) > Math.abs(verDist)) {
                if (horDist > 0) {
                    // go left
                    if (gameState.getGhostsMoves().get(ghost).contains(Direction.LEFT))
                        return Direction.LEFT;
                }
                if (horDist < 0) {
                    // go right
                    if (gameState.getGhostsMoves().get(ghost).contains(Direction.RIGHT))
                        return Direction.RIGHT;
                }
            }

            if (Math.abs(verDist) > Math.abs(horDist)) {
                if (verDist > 0) {
                    // go up
                    if (gameState.getGhostsMoves().get(ghost).contains(Direction.UP))
                        return Direction.UP;
                }
                if (verDist < 0) {
                    // go down
                    if (gameState.getGhostsMoves().get(ghost).contains(Direction.DOWN))
                        return Direction.DOWN;
                }
            }


            int randIndex = rand.nextInt(gameState.getGhostsMoves().get(ghost).size());
            return gameState.getGhostsMoves().get(ghost).get(randIndex);

            // If red is faced with more than one possibility he chooses the direction
            // with the highest priority (from highest to lowest: up. Left. Down. Right).
//            if (Math.abs(verDist) == Math.abs(horDist)) {
                // go up
//                if (gameState.getGhostsMoves().get(ghost).contains(Direction.UP))
//                    return Direction.UP;
//                // go left
//                if (gameState.getGhostsMoves().get(ghost).contains(Direction.LEFT))
//                    return Direction.LEFT;
//                // go down
//                if (gameState.getGhostsMoves().get(ghost).contains(Direction.DOWN))
//                    return Direction.DOWN;
//                // go right
//                if (gameState.getGhostsMoves().get(ghost).contains(Direction.RIGHT))
//                    return Direction.RIGHT;
//            }
        }
        
//        return Direction.NEUTRAL;
    }

    @Override
    public Map<Ghost, Direction> direction(GameState gameState)
    {
        this.gameState = gameState;
        
        Map<Ghost, Direction> ghostsMoves = new HashMap<Ghost, Direction>();

        for (Ghost ghost : gameState.getGhosts())
        {
            // Blinky
            if (ghost.getType() == GhostType.BLINKY)
                ghostsMoves.put(ghost, blinkyDir(ghost));

            // Pinky
            else if (ghost.getType() == GhostType.PINKY)
                ghostsMoves.put(ghost, pinkyDir(ghost));

            // Inky
            else if (ghost.getType() == GhostType.INKY)
               ghostsMoves.put(ghost, inkyDir(ghost, gameState.getGhosts().get(0))); // TODO: ugly fix

            // Sue
            else if (ghost.getType() == GhostType.SUE)
                ghostsMoves.put(ghost, sueDir(ghost));
        }

        return ghostsMoves;
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


    @Override
    public void drawUnderPills(Graphics2D dbg2D, int startX, int startY, int nodeSize)
    {
        for (Ghost ghost : gameState.getGhosts()) 
        {
            if (ghost.getType() == GhostType.BLINKY) {
                int ghostX = ghost.getCurrentNode().getX();
                int ghostY = ghost.getCurrentNode().getY();
                int pacmanX = gameState.getPacman().getCurrentNode().getX();
                int pacmanY = gameState.getPacman().getCurrentNode().getY();
                int horDist = Math.abs(ghostX - pacmanX);
                int verDist = Math.abs(ghostY - pacmanY);

                // horizontal distance
                dbg2D.setColor(Color.GREEN);
                dbg2D.drawLine(ghostX * nodeSize + startX, ghostY * nodeSize + startY,
                        pacmanX * nodeSize + startX, ghostY * nodeSize + startY);
                dbg2D.drawString(String.valueOf(horDist), (ghostX - (ghostX - pacmanX)) * nodeSize + startX,
                        ghostY * nodeSize + startY);


                // vertical distance
                dbg2D.setColor(Color.WHITE);
                dbg2D.drawLine(ghostX * nodeSize + startX, ghostY * nodeSize + startY,
                        ghostX * nodeSize + startX, pacmanY * nodeSize + startY);
                dbg2D.drawString(String.valueOf(verDist), ghostX * nodeSize + startX,
                        (ghostY - (ghostY - pacmanY)) * nodeSize + startY);
            }
        }
    }

    @Override
    public void drawAbovePills(Graphics2D dbg2D) {

    }

    @Override
    public void drawAboveEverything(Graphics2D dbg2D) {
        
    }

}