package dr.games.mspacman.agents.mcpaths;

import dr.games.mspacman.model.Node;
import dr.util.tree.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class PacPath {

    private List<TreeNode<Node>> treeNodes;
    public int pillCount = 0;

    public PacPath() {
        treeNodes = new ArrayList<TreeNode<Node>>();
    }

    public void addTreeNode(TreeNode<Node> tn) {
        treeNodes.add(tn);
    }

    public List<Node> elementList() {
        List<Node> nodes = new ArrayList<Node>();
        for (TreeNode<Node> tn : treeNodes)
            nodes.add(tn.getElement());
        return nodes;
    }

    public TreeNode<Node> firstTreeNode() {
        return treeNodes.get(1);
    }

    public List<TreeNode<Node>> getTreeNodes() {
        return treeNodes;
    }

    public TreeNode<Node> lastTreeNode() {
        return treeNodes.get(treeNodes.size() - 1);
    }

    public void setTreeNodes(List<TreeNode<Node>> treeNodes) {
        this.treeNodes = treeNodes;
    }

}
