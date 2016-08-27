package dr.games.mspacman.agents.treebased;//package net.davidrobles.games.pacsim.agents.treebased;
//
//import net.davidrobles.games.pacsim.model.AStarNode;
//import net.davidrobles.util.tree.Tree;
//import net.davidrobles.util.tree.TreeNode;
//
//import java.util.*;
//
//public class TreeP extends Tree<AStarNode> {
//
//    private List<Path> paths = new ArrayList<Path>();
//
//    public TreeP(TreeNode<AStarNode> root) {
//        super(root);
//    }
//
//    public List<Path> getPaths() {
//        return paths;
//    }
//
//    public static boolean isPathSafe(Path path) {
//        if (path.ghostsTotal() > 0) {
//            return false;
//        }
//        for (TreeNodeP ghostTreeNodeP : path.firstTreeNode().ghostDescendants()) {
//            TreeNodeP target = ghostTreeNodeP.pathSearch(path);
//            if (ghostTreeNodeP.depth() - target.depth() - 6 < target.depth()) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public static List<TreeNodeP> listToTreeNodePacman(List<TreeNode> nodes) {
//        List<TreeNodeP> list = new ArrayList<TreeNodeP>();
//        for (TreeNode tn : nodes)
//            list.add((TreeNodeP) tn);
//        return list;
//    }
//
//    public void makePaths() {
//        for (TreeNode<AStarNode> tn : externals()) {
//            Path path = new Path();
//            List<TreeNodeP> list = new ArrayList<TreeNodeP>();
//            list.add((TreeNodeP)tn);
//            list.addAll(1, listToTreeNodePacman(tn.ancestors()));
//            Collections.reverse(list);
//            path.setTreeNodes(list);
//            path.calculateNodeTypes();
//            paths.add(path);
//        }
//    }
//
//    public int safePaths() {
//        int reachablePaths  = 0;
//        for (Path path : paths)
//            if (path.isSafeness())
//                reachablePaths++;
//        return reachablePaths;
//    }
//
//}