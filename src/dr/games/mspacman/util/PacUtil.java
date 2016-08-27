package dr.games.mspacman.util;

import dr.games.mspacman.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class PacUtil {

    public static final Random rand = new Random();

    private PacUtil() { }

    public static Map<Ghost, Direction> ghostNeutralMoves(List<Ghost> ghosts)
    {
        Map<Ghost, Direction> ghostsNeutralMoves = new HashMap<Ghost, Direction>();

        for (Ghost ghost : ghosts) {
            ghostsNeutralMoves.put(ghost, Direction.NEUTRAL);
            ghostsNeutralMoves.put(ghost, Direction.NEUTRAL);
            ghostsNeutralMoves.put(ghost, Direction.NEUTRAL);
            ghostsNeutralMoves.put(ghost, Direction.NEUTRAL);            
        }

        return ghostsNeutralMoves;
    }

    public static BufferedImage getImage(String file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("MsPacMan/images/" + file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static Direction directionToNeighbor(Node current, Node neighbor) {
        // Up
        if (neighbor.getY() < current.getY())
            return Direction.UP;
            // Down
        else if (neighbor.getY() > current.getY())
            return Direction.DOWN;
            // Right
        else if (neighbor.getX() > current.getX()) {
            // Left Tunnel
            if (neighbor.getX() > current.getX() + 1)
                return Direction.LEFT;
            return Direction.RIGHT;
        }
        // Left
        else if (neighbor.getX() < current.getX()) {
            // RightTunnel
            if (neighbor.getX() < current.getX() - 1)
                return Direction.RIGHT;
            return Direction.LEFT;
        } else return null;
    }

    // Returns the next node by taking the specified direction 
    public static Node nextNode(Node src, Direction dir) {
        for (Node dest : src.getNeighbors())
            if (directionToNeighbor(src, dest) == dir)
                return dest;
        return null;
    }

    public static Direction direction(Agent cs, List<Direction> dirs)
    {
        Direction curDir = cs.getDirection();
        Direction pickedDir;

        do {
            pickedDir = dirs.get(rand.nextInt(dirs.size()));
        } while (pickedDir.opposite() == curDir);

        return pickedDir;
    }

    public static Node nearestPill(Node src, MazeState mazeState)
    {
        Node pillNode = null;
        int bestDist = Integer.MAX_VALUE;

        // for every pill node container
        for (Node n : mazeState.getMaze().getPillsNodes())
        {
            // is there a pill in the node
            if (mazeState.getPillsBitSet().get(n.getPillIndex()))
            {
                // if the distance from pacman to the node is shorter than the previous best
                if (mazeState.getMaze().distance(src, n) < bestDist) {
                    pillNode = n;
                    bestDist = mazeState.getMaze().distance(src, n);
                }
            }
        }

        return pillNode;
    }

    public static Node nearestPowerPill(Node src, MazeState mazeState)
    {
        Node powerPillNode = null;
        int bestDist = Integer.MAX_VALUE;

        // for every pill node container
        for (Node n : mazeState.getMaze().getPowerPillsNodes())
        {
            // is there a pill in the node
            if (mazeState.getPowersBitSet().get(n.getPowerIndex()))
            {
                // if the distance from pacman to the node is shorter than the previous best
                if (mazeState.getMaze().distance(src, n) < bestDist) {
                    powerPillNode = n;
                    bestDist = mazeState.getMaze().distance(src, n);
                }
            }
        }

        return powerPillNode;
    }

    public static Direction direction(Node src, Node dest, Maze maze)
    {
        Direction bestDir = null;
        int bestDirValue = Integer.MAX_VALUE;

        for (Node neighbor : src.getNeighbors())
        {
            Direction dirToNeighbor = directionToNeighbor(src, neighbor);

            // no turn back
            if (dirToNeighbor != dirToNeighbor.opposite())
            {
                // if this move leads to a shortest paths
                if (maze.distance(neighbor, dest) < bestDirValue) {
                    bestDir = dirToNeighbor;
                    bestDirValue = maze.distance(neighbor, dest);
                }
            }
        }

        return bestDir;
    }

    public static void search(List<Node> allNodes, int[] distances, int curNodeIndex, int curDist)
    {
        if (curDist < distances[curNodeIndex])
        {
            distances[curNodeIndex] = curDist;

            for (Node n : allNodes.get(curNodeIndex).getNeighbors()) {
                search(allNodes, distances, n.getNodeIndex(), curDist + 1);
            }
        }
    }

}
