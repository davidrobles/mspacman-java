package dr.games.mspacman.model;

import java.util.*;

public class Node {

    private int x;
    private int y;
    private int nodeIndex = -1;
    private int pillIndex = -1;
    private int powerIndex = -1;
    private List<Node> neighbors = new ArrayList<Node>();

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Node indexes

    public int getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public int getPillIndex() {
        return pillIndex;
    }

    public void setPillIndex(int pillIndex) {
        this.pillIndex = pillIndex;
    }

    public int getPowerIndex() {
        return powerIndex;
    }

    public void setPowerIndex(int powerIndex) {
        this.powerIndex = powerIndex;
    }

    public boolean isPillContainer() {
        return pillIndex >= 0;
    }

    public boolean isPowerPillContainer() {
        return powerIndex >= 0;
    }

    public List<Node> getNeighbors() {
        return neighbors;
    }

    // Regular methods

    public void addNeighbor(Node pathNode) {
        neighbors.add(pathNode);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Node node = (Node) o;

        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return x * y;
    }

    @Override
    public String toString() {
        return "Node: " + x + ", " + y;
    }

}