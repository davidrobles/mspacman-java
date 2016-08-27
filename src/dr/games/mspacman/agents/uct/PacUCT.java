package dr.games.mspacman.agents.uct;

import dr.games.mspacman.model.Direction;
import dr.games.mspacman.model.GameState;
import dr.games.mspacman.model.Node;
import dr.games.mspacman.model.PacAgent;
import dr.games.mspacman.view.AgentView;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class PacUCT implements PacAgent, AgentView
{
    private PacUCTNode root;
    private int currentScore;
    private int currentLives;

    private static final int SIMULATIONS = 500;
    public static final int MAX_DEPTH = 80;

    private static final Random rand = new Random();

    @Override
    public Direction direction(GameState gameState)
    {
        // init the root node to the current state
        root = new PacUCTNode(gameState, null, 0);

        // get current game state info
        currentScore = root.getGameState().getScore();
        currentLives = root.getGameState().getLives();

        // run the simulations
        for (int i = 0; i < SIMULATIONS; i++)
            playOneSequence(root);

        return getBestMove(root);
    }

    private void playOneSequence(PacUCTNode root)
    {
        LinkedList<PacUCTNode> list = new LinkedList<PacUCTNode>();
        list.add(root);
        
        // play for 30 time steps only
        while (list.getLast().getDepth() < MAX_DEPTH) {
            PacUCTNode selectedChild = descendByUCB1(list.getLast());
            if (selectedChild != null) // TODO: dont know why this works, if it does
                list.add(selectedChild);
            else
                break;
        }

        // evaluate the game state
        double reward = getValueByMC(list.getLast().getGameState());
        list.getLast().update(reward);

        updateValue(list, reward);
    }

    private double getValueByMC(GameState gameState)
    {
        // lost a life during the played sequence?
        if (currentLives > gameState.getLives())
            return 0;

        double rew = (gameState.getScore() - currentScore + 100) / 2000.0;
        
        // returns the points obtained during the simulation
        return Math.min(rew, 1.0);
    }

    private double getMaxScore() {
        double score = 0;
        score += (220) * 10;
        score += (50) * 4;
        score += ((200 + 400 + 800 + 1600)) * 4.0;
        return score;
    }

    private PacUCTNode descendByUCB1(PacUCTNode parentNode)
    {
//        int visits = 0;
//
//        for (PacUCTNode n : node.getNextGameStates())
//            visits += n.visits;
//
//        PacUCTNode maxNode = null;
//        Double maxValue = Double.NEGATIVE_INFINITY;
//
//        for (PacUCTNode nextGameState : node.getNextGameStates())
//        {
//            // ensured each "arm" be selected once before further exploration
//            if (nextGameState.visits == 0) {
//                nextGameState.nv = Double.MAX_VALUE;
//            }
//            // apply formula UCB1
//            else {
//                nextGameState.nv = (-nextGameState.value / (double) nextGameState.visits)
//                        + Math.sqrt((2.0 * Math.log((double) visits)) / (double) nextGameState.visits);
//            }
//
//            // update the best node
//            if (nextGameState.nv > maxValue) {
//                maxNode = nextGameState;
//                maxValue = nextGameState.nv;
//            }
//        }
//
//        return maxNode;

        PacUCTNode bestNode = null;
        Double bestValue = Double.MIN_VALUE;

        for (PacUCTNode childNode : parentNode.getNextGameStates())
        {
            // UCB1 value for this child
            double ucb1Value;

            // ensures each arm be selected once before further exploration
            if (childNode.getVisits() == 0) {
                ucb1Value = Double.MAX_VALUE - rand.nextInt(1000);
            }

            // applies the formula UCB1
            else {
                ucb1Value = (childNode.getValue() / (double) childNode.getVisits())
                        + Math.sqrt((2.0 * Math.log((double) parentNode.getVisits()))
                        / (double) childNode.getVisits());
            }

            // update to the best move
            if (ucb1Value > bestValue) {
                bestNode = childNode;
                bestValue = ucb1Value;
            }
        }

        return bestNode;

    }

    private void updateValue(LinkedList<PacUCTNode> nodes, double value)
    {
        // after each sequence, the value of played arm of each bandit is updated
        // iteratively from the father-node of the leaf to the root by formula UCB1.
        for (int i = nodes.size() - 2; i >= 0; i--)
        {
            // adds the value from the father-node back to the previous game states
            nodes.get(i).update(value);
        }
    }

    private Direction getBestMove(PacUCTNode node)
    {
        PacUCTNode bestChild = null;
        double bestVisits = -1;

        // for each next move from the current game state
        for (PacUCTNode nextStateNode : node.getNextGameStates())
        {
            // updates the best move to the one with most ucb visits
            if (nextStateNode.getVisits() > bestVisits) {
                bestChild = nextStateNode;
                bestVisits = nextStateNode.getVisits();
            }
        }

        assert bestChild != null;

        return bestChild.getDir();
    }

    @Override
    public void drawUnderPills(Graphics2D dbg2D, int offsetX, int offsetY, int nodeSize)
    {
        if (root != null) {
            drawNode(dbg2D, root, offsetX, offsetY, nodeSize);
        }
    }

    private void drawNode(Graphics2D dbg2D, PacUCTNode pacUCTNode, int offsetX, int offsetY, int nodeSize)
    {
        // green
        dbg2D.setColor(new Color(0, 255, 0));
        Node node = pacUCTNode.getGameState().getPacman().getCurrentNode();
        dbg2D.fillRect(node.getX() * nodeSize + offsetX, node.getY() * nodeSize + offsetY, nodeSize, nodeSize);
        // red
        dbg2D.setColor(new Color(((pacUCTNode.getVisits() * 255) / SIMULATIONS), 0, 0, 60));
        dbg2D.fillRect(node.getX() * nodeSize + offsetX, node.getY() * nodeSize + offsetY, 10, 10);

        // draw the full tree of next game states recursively
        for (PacUCTNode nextState : pacUCTNode.getNextGameStates())
            drawNode(dbg2D, nextState, offsetX, offsetY, nodeSize);
    }

    @Override
    public void drawAbovePills(Graphics2D dbg2D) {

    }

    @Override
    public void drawAboveEverything(Graphics2D dbg2D) {
        
    }

}
