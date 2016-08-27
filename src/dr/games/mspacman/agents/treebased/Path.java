package dr.games.mspacman.agents.treebased;//package net.davidrobles.games.pacsim.agents.treebased;
//
//import java.util.List;
//import java.util.ArrayList;
//
//public class Path implements Comparable<Path> {
//
//    private double value;
//    private boolean safeness;
//    private int emptyNodes;
//    private int pillNodes;
//    private int powerPillNodes;
//    private int ghostNodes;
//    private int edibleGhostNodes;
//    private List<TreeNodeP> treeNodes;
//
//    public Path() {
//        treeNodes = new ArrayList<TreeNodeP>();
//    }
//
//    public void addTreeNode(TreeNodeP tn) {
//        treeNodes.add(tn);
//    }
//
//    public void calculateNodeTypes() {
//        for (TreeNodeP tn : treeNodes) {
//            if (tn.getType() == TreeNodePType.EMPTY)
//                emptyNodes++;
//            else if (tn.getType() == TreeNodePType.GHOST)
//                ghostNodes++;
//            else if (tn.getType() == TreeNodePType.PILL)
//                pillNodes++;
//            else if (tn.getType() == TreeNodePType.POWER_PILL)
//                powerPillNodes++;
//            else if (tn.getType() == TreeNodePType.EDIBLE_GHOST)
//                edibleGhostNodes++;
//        }
//    }
//
//    public double getValue() {
//        return value;
//    }
//
//    public int getEmptyNodes() {
//        return emptyNodes;
//    }
//
//    public int ghostsTotal() {
//        return ghostNodes;
//    }
//
//    public TreeNodeP firstTreeNode() {
//        return treeNodes.get(1);
//    }
//
//    public boolean isEmpty() {
//        return pillNodes == 0 && powerPillNodes == 0;
//    }
//
//    public boolean isSafeness() {
//        return safeness;
//    }
//
//    public int getEdibleGhostNodes() {
//        return edibleGhostNodes;
//    }
//
//    public int getPillNodes() {
//        return pillNodes;
//    }
//
//    public int getPowerPillNodes() {
//        return powerPillNodes;
//    }
//
//    public List<TreeNodeP> getTreeNodes() {
//        return treeNodes;
//    }
//
//    public TreeNodeP lastTreeNode() {
//        return treeNodes.get(treeNodes.size() - 1);
//    }
//
//    public void printNodeTypes() {
//        System.out.println("Empty Nodes:      " + emptyNodes);
//        System.out.println("Ghost Nodes:      " + ghostNodes);
//        System.out.println("Pill Nodes:       " + pillNodes);
//        System.out.println("Power Pill Nodes: " + powerPillNodes);
//        System.out.println("REACHABLE:" + safeness);
//    }
//
//    public void setSafeness(boolean safeness) {
//        this.safeness = safeness;
//    }
//
//    public void setTreeNodes(List<TreeNodeP> treeNodes) {
//        this.treeNodes = treeNodes;
//    }
//
//    public void setValue(double value) {
//        this.value = value;
//    }
//
//    @Override
//    public int compareTo(Path path) {
//        if (this.getValue() > path.getValue())
//            return -1;
//        if (this.getValue() < path.getValue())
//            return 1;
//        return 0;
//    }
//}