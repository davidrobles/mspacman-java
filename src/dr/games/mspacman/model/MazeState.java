package dr.games.mspacman.model;

import java.util.BitSet;

public class MazeState {

    private Maze maze;
    private BitSet pillsBitSet;
    private BitSet powersBitSet;

    public MazeState() { }

    public MazeState(MazeState mazeState) {
        this.maze = mazeState.maze;
        this.pillsBitSet = (BitSet) mazeState.pillsBitSet.clone();
        this.powersBitSet = (BitSet) mazeState.powersBitSet.clone();
    }

    public Maze getMaze() {
        return maze;
    }

    public BitSet getPillsBitSet() {
        return pillsBitSet;
    }

    public BitSet getPowersBitSet() {
        return powersBitSet;
    }

    public void setMaze(Maze maze)
    {
        this.maze = maze;

        // init pills
        pillsBitSet = new BitSet(maze.getPillsNodes().size());
        pillsBitSet.set(0, maze.getPillsNodes().size());

        // init powers
        powersBitSet = new BitSet(maze.getPowerPillsNodes().size());
        powersBitSet.set(0, maze.getPowerPillsNodes().size());
    }

}
