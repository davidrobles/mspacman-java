package dr.games.mspacman.agents.mcpaths;

import dr.games.mspacman.agents.ProGhostsController;
import dr.games.mspacman.model.*;
import dr.games.mspacman.util.PacUtil;
import dr.games.mspacman.view.AgentView;
import dr.util.tree.Tree;
import dr.util.tree.TreeNode;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PathMCAgent implements PacAgent, AgentView {

    private Tree<Node> tree;
    private List<PacPath> paths = new ArrayList<PacPath>();
    private static final int GAME_TICKS_PER_GAME = 30;
    private static final int PLAYS_PER_MOVE = 50;
    private static final int TREE_HEIGHT = 30;

    private static final ProGhostsController ghostsController = new ProGhostsController(100, 100, 100, 100);

    @Override
    public Direction direction(GameState gameState) // remove this parameter
    {
        createTree(new TreeNode<Node>(gameState.getPacman().getCurrentNode()));
        createPaths();
        countPills(gameState);
        Collections.shuffle(paths);

        PacPath bestPath = null;
        double bestPathScore = Double.MIN_VALUE;

        for (PacPath path : paths) {

            double sum = 0;

            // Play random games for every path
            for (int i = 0; i < PLAYS_PER_MOVE; i++)
            {
                GameState playGS = gameState.copy();
                List<Node> pathNodes = path.elementList();
                pathNodes.remove(0);
                int curScore = playGS.getScore();
                boolean liveLost = false;
                int maxTimeStep = playGS.getTimeSteps() + GAME_TICKS_PER_GAME;

                // Play until the end of the game or max time steps
                while (!liveLost && playGS.getTimeSteps() < maxTimeStep)
                {
                    // lives before move
                    int livesBefore = playGS.getLives();

                    Direction pacDir =
                            PacUtil.directionToNeighbor(playGS.getPacman().getCurrentNode(), pathNodes.get(0));
//                    playGS.nextState(pacDir, PacUtil.ghostNeutralMoves(playGS.getGhosts()));
                    playGS.nextState(pacDir, ghostsController.direction(playGS));
                    assert pathNodes.size() > 0;
                    pathNodes.remove(0);
                    // lives after move
                    int livesAfter = playGS.getLives();
                    // if lost live
                    if (livesBefore != livesAfter)
                        liveLost = true;
                }
//                int playScore = !liveLost ? playGS.getScore() - curScore : -100;
                int playScore = evaluatePlay(playGS, liveLost, curScore);
                sum += playScore;
            }

            if (sum == 0) {
				// choose the adjacent current with the
				// nearest pill
				// check that copying works!
				Node current = gameState.getPacman().getCurrentNode();
                Node nearestPillNode = PacUtil.nearestPill(current, gameState.getMazeState());

                Direction dir = PacUtil.direction(current, nearestPillNode, gameState.getMazeState().getMaze());
                Direction dir2 = PacUtil.directionToNeighbor(current, path.firstTreeNode().getElement());

                if (dir == dir2) {
                    sum = 1;
                }

			}

            // update the best path
            if (bestPath == null || sum > bestPathScore) {
                bestPath = path;
                bestPathScore = sum;
            }
        }

        if (bestPath != null)
            return PacUtil.directionToNeighbor(gameState.getPacman().getCurrentNode(),
                    bestPath.firstTreeNode().getElement());
        
        return Direction.LEFT;

    }


    private void createTree(TreeNode<Node> root) {
        tree = new Tree<Node>(root);
        createTreeHelper(root, null);
    }

    private void createTreeHelper(TreeNode<Node> treeNode, Node previous) {
        if (treeNode.depth() < TREE_HEIGHT) {
            List<Node> neighbors = treeNode.getElement().getNeighbors();
            for (Node n : neighbors) {
                if (!n.equals(previous)) {
                    TreeNode<Node> child = new TreeNode<Node>(n);
                    treeNode.addChild(child);
                    createTreeHelper(child, treeNode.getElement());
                }
            }
        }
    }

    private void createPaths() {
        paths.clear();
        for (TreeNode<Node> tn : tree.leafs()) {
            PacPath path = new PacPath();
            List<TreeNode<Node>> list = new ArrayList<TreeNode<Node>>();
            list.add(tn);
            list.addAll(1, tn.ancestors());
            Collections.reverse(list);
            path.setTreeNodes(list);
            paths.add(path);
        }
    }

    private void countPills(GameState gameState) {
        for (PacPath p : paths) {
            for (TreeNode<Node> tn : p.getTreeNodes()) {
                Node n = tn.getElement();
                if (n.getPillIndex() != -1 && gameState.getMazeState().getPillsBitSet().get(n.getPillIndex())) {
                    p.pillCount++;
                }
            }
        }
    }

    int evaluatePlay(GameState gameState, boolean liveLost, int startScore) {
        return !liveLost ? gameState.getScore() - startScore : -1000;
    }

    public void drawTree(TreeNode<Node> tn, Graphics2D dbg2D, int xOffset, int yOffset, int nodeSize)
    {
        if (tn.isRoot())
            dbg2D.setColor(Color.RED);
        else
            dbg2D.setColor(Color.GREEN);

        dbg2D.fillRect(tn.getElement().getX() * nodeSize + xOffset,
                (tn.getElement().getY() * nodeSize) + yOffset, nodeSize, nodeSize);

        for (TreeNode<Node> t : tn.getChildren())
            drawTree(t, dbg2D, xOffset, yOffset, nodeSize);
    }

    @Override
    public void drawUnderPills(Graphics2D dbg2D, int xOffset, int yOffset, int nodeSize) {
        if (tree.getRoot() != null)
            drawTree(tree.getRoot(), dbg2D, xOffset, yOffset, nodeSize);
    }

    @Override
    public void drawAbovePills(Graphics2D dbg2D) {

    }

    @Override
    public void drawAboveEverything(Graphics2D dbg2D) {

    }
    
}
