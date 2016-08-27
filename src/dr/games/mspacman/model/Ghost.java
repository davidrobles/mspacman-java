package dr.games.mspacman.model;

public class Ghost extends Agent {

    private GhostType type;
    private int edibleTime = 0;

    public Ghost(GhostType type) {
        this.type = type;
    }

    // Copy constructor
    public Ghost(Ghost ghost) {
        super(ghost);
        this.type = ghost.type;
        this.edibleTime = ghost.edibleTime;
    }

    public GhostType getType() {
        return type;
    }

    public int getEdibleTime() {
        return edibleTime;
    }

    public void setEdibleTime(int edibleTime) {
        this.edibleTime = edibleTime;
    }

    public boolean isEdible() {
        return edibleTime > 0;
    }

    @Override
    public String toString() {
        return type.toString();
    }

}
