package dr.games.mspacman.model;

public class Agent {

    private Maze maze;
    private Direction direction = Direction.LEFT;
    private int currentNodeIndex;

    public Agent() { }

    // Copy constructor
    public Agent(Agent cs) {
        this.maze = cs.maze;
        this.direction = cs.direction;
        this.currentNodeIndex = cs.currentNodeIndex;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Node getCurrentNode() {
        return maze.getNodes().get(currentNodeIndex);
    }

    public void setCurrentNode(Node node) {
        currentNodeIndex = node.getNodeIndex();
    }

    public int getCurrentNodeIndex() {
        return currentNodeIndex;
    }

    public void setCurrentNodeIndex(int currentNodeIndex) {
        this.currentNodeIndex = currentNodeIndex;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

}
