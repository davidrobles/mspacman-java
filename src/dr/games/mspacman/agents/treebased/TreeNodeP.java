package dr.games.mspacman.agents.treebased;//package net.davidrobles.games.pacsim.agents.treebased;
//
//import net.davidrobles.util.tree.TreeNode;
//import net.davidrobles.games.pacsim.model.AStarNode;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Queue;
//import java.util.LinkedList;
//
//public class TreeNodeP extends TreeNode<AStarNode>  {
//
//    // Highlights a node for testing purporses. BAD CODE
//    private boolean highlight = false;
//
//    private TreeNodePType type;
//
//    public TreeNodeP(AStarNode node) {
//        super(node);
//    }
//
//    public TreeNodeP(AStarNode node, TreeNodePType type) {
//        super(node);
//        this.type = type;
//        children = new ArrayList<TreeNode<AStarNode>>();
//    }
//
//    public TreeNodePType getType() {
//        return type;
//    }
//
//    public int hasDescendantGhost(List<TreeNodeP> ghostNodes) {
//        for (TreeNodeP tnp : ghostNodes) {
//            int temp = hasDescendant(tnp);
//            if (temp > 0) {
//                return temp;
//            }
//        }
//        return 0;
//    }
//
//    public boolean isHighlight() {
//        return highlight;
//    }
//
//    public void setHighlight(boolean highlight) {
//        this.highlight = highlight;
//    }
//
//    public int closerGhostDistance() {
//        if (closerGhost() != null)
//            return closerGhost().depth();
//        return 0;
//    }
//
//    public TreeNodeP closerGhost() {
//        Queue<TreeNodeP> queue = new LinkedList<TreeNodeP>();
//        queue.offer(this);
//        while (!queue.isEmpty()) {
//            TreeNodeP examined = queue.poll();
//            if (examined.getType() == TreeNodePType.GHOST) {
//                return examined;
//            } else {
//                for (TreeNode tn : examined.getChildren()) {
//                    TreeNodeP tnp = (TreeNodeP) tn;
//                    queue.add(tnp);
//                }
//            }
//        }
//        return null;
//    }
//
//    public int hasDescendantGhost() {
//        return hasDescendant(this, 1);
//    }
//
//    private int hasDescendant(TreeNodeP anc, int i) {
//        for (TreeNode child : anc.getChildren()) {
//            TreeNodeP child2 = (TreeNodeP) child;
//            int temp = hasDescendant(child2, i + 1);
//            if (child2.getType() == TreeNodePType.GHOST || temp > i) {
//                return temp;
//            }
//        }
//        return i - 1;
//    }
//
//    public TreeNodeP pathSearch(Path path) {
//        return pathSearch(this, path);
//    }
//
//    public TreeNodeP pathSearch(TreeNodeP current, Path path) {
//        if (path.getTreeNodes().contains(current))
//            return current;
//        if (current.getParent() == null)
//            return null;
//        return pathSearch((TreeNodeP)current.getParent(), path);
//    }
//
//    public List<TreeNodeP> ghostDescendants() {
//        List<TreeNodeP> list = new ArrayList<TreeNodeP>();
//        ghostDescendants(list, this);
//        return list;
//    }
//
//    public void ghostDescendants(List<TreeNodeP> list, TreeNodeP current) {
//        if (current.getType() == TreeNodePType.GHOST)
//            list.add(current);
//        for (TreeNode tn : current.getChildren())
//            ghostDescendants(list, (TreeNodeP)tn);
//    }
//
//}