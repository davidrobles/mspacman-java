package dr.games.mspacman.agents.mc;

import dr.games.mspacman.agents.uct.PacUCTNode;
import dr.games.mspacman.model.*;
import dr.games.mspacman.util.PacUtil;
import dr.games.mspacman.view.AgentView;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PureMC implements PacAgent, AgentView {

    private static final int PLAYS_PER_MOVE = 30;
    private static final int GAME_TICKS_PER_GAME = 40;

//    private static GameStateView view = GameStateView.newGameStateViewFramed(); // TODO: temp view

    private Map<Node, Integer> nodeValues = new HashMap<Node, Integer>();

    @Override
    public Direction direction(GameState gameState)
    {
        nodeValues.clear();
        Direction bestDir = null;
        double bestDirScore = Double.MIN_VALUE;

        for (Direction moveDir : gameState.getPacmanMoves())
        {
            double sum = 0;

            // Play simulations on this direction, never going back to the previous node
            for (int i = 0; i < PLAYS_PER_MOVE; i++)
            {
                // visited nodes during the simulation
                List<Node> visitedNodes = new ArrayList<Node>();
                
                GameState playGS = gameState.copy();
                int origScore = playGS.getScore();
                int maxTimeStep = playGS.getTimeSteps() + GAME_TICKS_PER_GAME;

                // make the first move
                playGS.nextState(moveDir, PacUtil.ghostNeutralMoves(playGS.getGhosts()));

                visitedNodes.add(playGS.getPacman().getCurrentNode());



//                view.gameStatedUpdated(gameState); // TODO: temp view


                
                // Play until lossing a life or 
                while (!playGS.isGameOver() && !playGS.lostLifeOnLastTimeStep()
                        && playGS.getTimeSteps() < maxTimeStep)
                {
                    List<Direction> possibleDirs = playGS.getPacmanMoves();
                    // remove the move that takes pac-man to the previous node
                    possibleDirs.remove(playGS.getPacman().getDirection().opposite());
                    // get a random legal move
                    Direction pacDir = possibleDirs.get(PacUtil.rand.nextInt(possibleDirs.size()));
                    playGS.nextState(pacDir, PacUtil.ghostNeutralMoves(playGS.getGhosts()));
                    visitedNodes.add(playGS.getPacman().getCurrentNode());
//                    view.gameStatedUpdated(playGS); // TODO: temp view
                }

                int playScore = evaluate(playGS, origScore);
                sum += playScore;

                // update value for the nodes
                for (Node visitedNode : visitedNodes) {
                    // node already in the map
                    if (nodeValues.containsKey(visitedNode)) {
                        Integer value = nodeValues.get(visitedNode);
                        nodeValues.put(visitedNode, value + playScore);
                    } else {
                        nodeValues.put(visitedNode, 0);
                    }
                }
            }

            // if the simulations for this move don't have any pills, power pills, edible ghosts
//            if (sum == 0)
//            {
//                // get the nearest pill
//                Node pacmanCurrentNode = gameState.getPacman().getCurrentNode();
//                Node nearestPillNode = PacUtil.nearestPill(pacmanCurrentNode, gameState.getMazeState());
//
//                Direction dir = PacUtil.direction(pacmanCurrentNode, nearestPillNode,
//                        gameState.getMazeState().getMaze());
//
//                if (dir == moveDir) {
//                    sum = 1;
//                }
//			}

            // update the best path
            if (bestDir == null || sum > bestDirScore) {
                bestDir = moveDir;
                bestDirScore = sum;
            }
        }

        return bestDir;
    }

    // evaluation function for the game state
    int evaluate(GameState gs, int startScore)
    {
        // if didn't lose a life during the simulation
        if (!gs.lostLifeOnLastTimeStep())
        {
            // return the score obtained during the simulation  
            return gs.getScore() - startScore;
        }

        // if pac-man lost during the simulation
        return -1000;
    }

    public float sigmoid(float value, float min, float max)
    {
        float range = Math.abs(max - min);
        return Math.max(0.0f, (Math.abs(value - min)) / range);
    }

    @Override
    public void drawUnderPills(Graphics2D dbg2D, int offsetX, int offsetY, int nodeSize)
    {    
        int maxValue = Integer.MIN_VALUE;
        int minValue = Integer.MAX_VALUE;
        
        for (Map.Entry<Node, Integer> entry : nodeValues.entrySet()) {
            maxValue = Math.max(maxValue, entry.getValue());
            minValue = Math.min(minValue, entry.getValue());
        }

        for (Map.Entry<Node, Integer> entry : nodeValues.entrySet()) {
            // Get entry
            Node node = entry.getKey();
            int value = entry.getValue();

            // Draw node
            dbg2D.setColor(new Color(0, 255, 0));
            dbg2D.fillRect(node.getX() * nodeSize + offsetX, node.getY() * nodeSize + offsetY, nodeSize, nodeSize);
//            dbg2D.setColor(new Color(((value * 255) / maxValue), 0, 0, 60));
            dbg2D.setColor(new Color(sigmoid(value, minValue, maxValue), 0f, 0f, 0.6f));
            dbg2D.fillRect(node.getX() * nodeSize + offsetX, node.getY() * nodeSize + offsetY, 10, 10);
        }
    }

    private void drawNode(Graphics2D dbg2D, PacUCTNode pacUCTNode, int offsetX, int offsetY, int nodeSize)
    {
//        dbg2D.setColor(new Color(0, 255, 0));
//        Node node = pacUCTNode.getGameState().getPacman().getCurrentNode();
//        dbg2D.fillRect(node.getX() * nodeSize + offsetX, node.getY() * nodeSize + offsetY, nodeSize, nodeSize);
//        dbg2D.setColor(new Color(((pacUCTNode.visits * 255) / SIMULATIONS), 0, 0, 60));
//        dbg2D.fillRect(node.getX() * nodeSize + offsetX, node.getY() * nodeSize + offsetY, 10, 10);
//
//        // draw the full tree of next game states recursively
//        for (PacUCTNode nextState : pacUCTNode.getNextGameStates())
//            drawNode(dbg2D, nextState, offsetX, offsetY, nodeSize);
    }

    @Override
    public void drawAbovePills(Graphics2D dbg2D) {

    }

    @Override
    public void drawAboveEverything(Graphics2D dbg2D) {

    }

}
