package dr.games.mspacman.agents.treebased;//package net.davidrobles.games.pacsim.agents.treebased;
//
//import net.davidrobles.util.tree.TreeNode;
//
//import java.awt.*;
//import java.util.Random;
//
///**
// * This class extends the game panel view and draws a tree.
// * @author David Robles
// */
//public class TreeDisplay extends GamePanel implements TreeBasedControllerObserver {
//
////    private final int NODE_SIZE = 6;
//
//    /**
//     * The color of the tree path. It has a little of transparency
//     * to avoid covering the pills and power pills
//     */
//    private static final Color PATH_COLOR = new Color(0.0f, 0.6f, 0.0f, 0.8f);
//
//    /**
//     * The color of a highlighted tree node in a path.
//     * Used for testing purposes.
//     */
//    private static final Color HIGHLIGHT_COLOR = new Color(1.0f, 0.0f, 0.0f, 1.0f);
//
//    public static Random rand = new Random();
//
//    /** The tree path that will be drawn */
//    private TreeP tree;
//
//    /**
//     * Creates a panel view of the model.
//     *
//     * @param gameModel the model of the game
//     */
//    public TreeDisplay(GameModelInterf gameModel) {
//        super(gameModel);
//    }
//
//    /**
//     * Creates a panel view of the model.
//     *
//     * @param game the model of the game
//     */
//    public TreeDisplay(Game game) {
//        super(game);
//    }
//
//    public void drawTree(TreeP tree) {
//        drawTree(tree, (TreeNodeP) tree.getRoot(), 0);
//    }
//
//    public void drawTree(TreeP tree, TreeNodeP tn, int c) {
//        for (TreeNode t : tn.getChildren()) {
//            TreeNodeP child = (TreeNodeP) t;
//            if (child.isHighlight()) {
//                dbg2D.setColor(HIGHLIGHT_COLOR);
//                dbg2D.fillRect(child.getElement().getX() * GamePanel.SCALE - GamePanel.SCALE, (child.getElement().getY() * GamePanel.SCALE - GamePanel.SCALE)
//                        + GamePanel.MENU_HEIGHT, GamePanel.SCALE + GamePanel.SCALE, GamePanel.SCALE + GamePanel.SCALE);
//            } else {
//                dbg2D.setColor(PATH_COLOR);
//                dbg2D.fillRect(child.getElement().getX() * GamePanel.SCALE, (child.getElement().getY() * GamePanel.SCALE)
//                        + GamePanel.MENU_HEIGHT, GamePanel.SCALE, GamePanel.SCALE);
//            }
//
////            dbg2D.fillRect(child.getElement().getX() * SCALE, (child.getElement().getY() * SCALE)
////                    + MENU_HEIGHT, SCALE, SCALE);
////            if (child.equals(tree.getGoNode())) {
////                dbg2D.setColor(Color.WHITE);
////            }
//            if (!child.isExternal()) {
//                drawTree(tree, child, c + 1);
//            }
//        }
//    }
//
//    @Override
//    protected void gameRender() {
//        super.gameRender();
//        if (tree != null) {
//            drawTree(tree);
//        }
//    }
//
//    @Override
//    public void increaseMoveTotal(TreeP tree) {
//        this.tree = tree;
//    }
//
//}