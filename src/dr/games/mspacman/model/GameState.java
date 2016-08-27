package dr.games.mspacman.model;

import dr.games.mspacman.util.PacUtil;

import java.util.*;

public class GameState {

    // Game States
    private MazeState mazeState;
    private Pacman pacman;
    private List<Ghost> ghosts;

    // Array of mazes to play. Resets back to first maze when completing them all
    private Maze[] mazes = new Maze[] {Maze.A, Maze.A, Maze.B, Maze.B, Maze.C, Maze.C, Maze.D, Maze.D};
//    private Maze[] mazes = new Maze[] {Maze.A/*, Maze.A, Maze.B, Maze.B, Maze.C, Maze.C, Maze.D, Maze.D*/};
    private int mazeIndex = -1;

    // State
    private int score;
    private int lives;
    private int timeSteps;
    private int mazeTimeSteps = 0;

//    private boolean[] naturalReversalFlags = { false, false, false };
    private boolean extraLifeAwarded = false;
    private boolean lostLifeOnLastTimeStep = false;

    // Constants
    private static final int START_LIVES = 2;
    private static final int PILL_POINTS = 10;
    private static final int POWER_PILL_POINTS = 50;
    private static final int EAT_GHOST_SCORE = 200;
    private static final int GHOST_COLLISION_RATIO = 3;
    private static final int EXTRA_LIFE_SCORE = 10000;
    private static final int[] NATURAL_REVERSALS = { 120, 600, 36000 };

    // Possible moves. For caching purposes
    private List<Direction> pacmanMoves = new ArrayList<Direction>();
    private Map<Ghost, List<Direction>> ghostsMoves = new HashMap<Ghost, List<Direction>>();

    // Observers of every time step played
    private List<GameEventsObserver> gameEventsObservers = new ArrayList<GameEventsObserver>();

    private static final Random rand = new Random();

    public boolean print = false;

    // Constructors

    private GameState() { }

    public GameState(MazeState mazeState, Pacman pacman, List<Ghost> ghosts)
    {
        this.mazeState = mazeState;
        this.pacman = pacman;
        this.ghosts = ghosts;
    }

    public static GameState newDefaultGameState()
    {
        GameState newGameState = new GameState();

        // init pacman
        newGameState.pacman = new Pacman();

        // init ghosts
        newGameState.ghosts = new ArrayList<Ghost>();
        newGameState.ghosts.add(new Ghost(GhostType.BLINKY));
        newGameState.ghosts.add(new Ghost(GhostType.PINKY));
        newGameState.ghosts.add(new Ghost(GhostType.INKY));
        newGameState.ghosts.add(new Ghost(GhostType.SUE));

        // init maze
        newGameState.mazeState = new MazeState();

        // init game
        newGameState.reset();

        return newGameState;
    }

    public boolean disablePrint() {
        return print;
    }

    public void enablePrint() {
        this.print = true;
    }

    public void printMessage(String string) {
        if (print)
            System.out.println(string);
    }

    // Getters and Setters

    public MazeState getMazeState() {
        return mazeState;
    }

    public void setMazeState(MazeState mazeState) {
        this.mazeState = mazeState;
    }

    public Pacman getPacman() {
        return pacman;
    }

    public void setPacman(Pacman pacman) {
        this.pacman = pacman;
    }

    public List<Ghost> getGhosts() {
        return ghosts;
    }

    public int getTimeSteps() {
        return timeSteps;
    }

    public int getMazeIndex() {
        return mazeIndex;
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }
    
    // Methods

    public void nextState(Direction pacDir, Map<Ghost, Direction> ghostsDirs)
    {
        if (isGameOver()) {
            System.out.println("The game is over! No more next states!");
            return;
        }

        printMessage("Time Step: " + (timeSteps + 1));

        updateEdibleTimer();

        // move the agents
        movePacMan(pacDir);
        moveGhosts(ghostsDirs);

        // checks for collisions with ghosts, and eats pills if possible
        if (!didPacmanCollideWithGhosts()) {
            eatPills();
            lostLifeOnLastTimeStep = false;
        } else {
            lostLifeOnLastTimeStep = true;
        }

        //  update the game views
        notifyGameStateObservers();

        // update time steps
        mazeTimeSteps++;
        timeSteps++;

        // if the maze was completed reset the appropriate stuff
        checkMazeCompletion();

        printMessage("------------------------------");

        // clear moves caching
        this.pacmanMoves.clear();
        this.ghostsMoves.clear();
    }

    public boolean lostLifeOnLastTimeStep() {
        return lostLifeOnLastTimeStep;
    }

    public boolean isGameOver() {
        return lives < 0;
    }    

    private void updateEdibleTimer()
    {
        for (Ghost g : ghosts)
            g.setEdibleTime(g.getEdibleTime() - 1);
    }

    private void movePacMan(Direction desiredDirection)
    {
        Direction moveDirection = null;

        // set the move direction to the desired direction
        if (getPacmanMoves().contains(desiredDirection)) {
            moveDirection = desiredDirection;
        }
        // set the move direction to the current direction if possible
        else if (getPacmanMoves().contains(pacman.getDirection())) {
            moveDirection = pacman.getDirection();
        }

        // move pacman if there is a move direction set
        if (moveDirection != null) {
            Node next = PacUtil.nextNode(pacman.getCurrentNode(), moveDirection);
            pacman.setCurrentNodeIndex(next.getNodeIndex());
            pacman.setDirection(moveDirection);
        }
    }

    public List<Direction> getPacmanMoves()
    {
        if (pacmanMoves.isEmpty()) {
            for (Node next : pacman.getCurrentNode().getNeighbors())
                pacmanMoves.add(PacUtil.directionToNeighbor(pacman.getCurrentNode(), next));
        }

        return pacmanMoves;
    }

    private void moveGhosts(Map<Ghost, Direction> moves)
    {
        for (Ghost ghost : getGhosts())
        {
            if (!ghost.isEdible() || (ghost.isEdible() && timeSteps % 2 == 0))
            {
                Direction moveDirection;
                Direction desiredDirection = moves.get(ghost);

                // set the move direction to the desired direction
                if (getGhostsMoves().get(ghost).contains(desiredDirection)) {
                    moveDirection = desiredDirection;
                }
                // choose a random legal move that continues on the same direction
                else {
                    List<Direction> legalMoves = getGhostMoves(ghost);
                    // removes the move that takes the ghost to the previous direction
                    legalMoves.remove(ghost.getDirection().opposite());
                    int randomMove = rand.nextInt(getGhostMoves(ghost).size());
                    moveDirection = getGhostMoves(ghost).get(randomMove);
                }

                assert moveDirection != null;

                Node next = PacUtil.nextNode(ghost.getCurrentNode(), moveDirection);
                ghost.setCurrentNodeIndex(next.getNodeIndex());
                ghost.setDirection(moveDirection);
            }
        }
    }

    public Map<Ghost, List<Direction>> getGhostsMoves()
    {
        if (ghostsMoves.isEmpty())
            for (Ghost ghost : getGhosts())
                ghostsMoves.put(ghost, getGhostMoves(ghost));

        return ghostsMoves;
    }

    public List<Direction> getGhostMoves(Ghost ghost)
    {
        List<Direction> dirs = new ArrayList<Direction>();

        for (Node next : ghost.getCurrentNode().getNeighbors())
        {
            Direction dir = PacUtil.directionToNeighbor(ghost.getCurrentNode(), next);

            if (dir != ghost.getDirection().opposite())
                dirs.add(dir);
        }

        return dirs;
    }

    // Reverses the ghosts directions if it's time.
    // It only reverses when the ghost are not edible.
//    private boolean naturalGhostsReversal()
//    {
//        for (int i = 0; i < NATURAL_REVERSALS.length; i++) {
//            if (mazeTimeSteps == NATURAL_REVERSALS[i]) {
//                naturalReversalFlags[i] = false;
//                return true;
//            }
//        }
//
//        return false;
//    }

    private void checkMazeCompletion()
    {
        // move to the next maze if there are no more pills and power pills on the maze
        if (mazeState.getPillsBitSet().isEmpty() && mazeState.getPowersBitSet().isEmpty()) {
            printMessage("Maze completed (index = " + mazeIndex + ")");
            nextMaze();
            resetGhosts();
            resetPacMan();
            mazeTimeSteps = 0;
//            resetNaturalReversalTimer();
        }
    }

    private void resetMazes()
    {
        mazeIndex = -1; // forces to start from level 1
        nextMaze();
    }

    private void nextMaze()
    {
        mazeIndex++;
        Maze curMaze = mazes[mazeIndex % mazes.length];
        mazeState.setMaze(curMaze);
        mazeTimeSteps = 0;
    }

    private void eatPills()
    {
        // if the current node of pacman is a pill node it eats it
        if (pacman.getCurrentNode().isPillContainer()
                && mazeState.getPillsBitSet().get(pacman.getCurrentNode().getPillIndex())) {
            mazeState.getPillsBitSet().set(pacman.getCurrentNode().getPillIndex(), false);
            printMessage("Pills remaining: " + mazeState.getPillsBitSet().cardinality());
            addPoints(PILL_POINTS);
        }
        // if the current node of pacman is a pill node it eats it
        else if (pacman.getCurrentNode().isPowerPillContainer()
                && mazeState.getPowersBitSet().get(pacman.getCurrentNode().getPowerIndex())) {
            mazeState.getPowersBitSet().set(pacman.getCurrentNode().getPowerIndex(), false);
            printMessage("Power Pills remaining: " + mazeState.getPowersBitSet().cardinality());
            addPoints(POWER_PILL_POINTS);
            reverseGhostsDirections();
            setGhostsEdible();
        }
    }

    private void addPoints(int points)
    {
        score += points;

        // Adds an extra life if the score reaches 10000 points
//        if (!extraLifeAwarded && score >= EXTRA_LIFE_SCORE) {
//            lives++;
//            extraLifeAwarded = true;
//        }
    }

    private void setGhostsEdible() {
        for (Ghost g : getGhosts())
            g.setEdibleTime(mazeState.getMaze().getEdibleTimer());
    }

    private void reverseGhostsDirections()
    {
        for (Ghost g : getGhosts())
            if (!g.isEdible())
                g.setDirection(g.getDirection().opposite());
    }

    // TODO: returns true???
    private boolean didPacmanCollideWithGhosts()
    {
        for (Ghost ghost : getGhosts())
        {
            // pacman collided with a ghost
            if (ghostCollision(ghost))
            {
                // eats a ghost
                if (ghost.isEdible()) {
                    eatGhost(ghost);
                }
                // eaten by a ghost
                else {
                    lossLife();
                    return true;
                }
            }
        }
        return false;
    }

    // Returns true if there is a collision between Pac-Man and the given ghost
    private boolean ghostCollision(Ghost ghost)
    {
        int distToGhost = mazeState.getMaze().distance(pacman.getCurrentNode(),
                ghost.getCurrentNode());

        return distToGhost < GHOST_COLLISION_RATIO;
    }

    private void eatGhost(Ghost ghost) {
        printMessage(ghost.getType() + " eaten!");
        notifyGhostEaten(ghost);
        addPoints(ghostEatenScore());
        // Reset ghost location on the maze
        resetGhost(ghost);
    }

    private void notifyGhostEaten(Ghost ghost) {

    }

    // Pacman was eaten by a ghost
    private void lossLife() {
        lives--;
        resetGhosts();
        resetPacMan();
//        resetNaturalReversalTimer();
    }

    private void resetGhosts() {
        for (Ghost ghost : ghosts)
            resetGhost(ghost);
    }

    private void resetGhost(Ghost ghost) {
        ghost.setMaze(mazeState.getMaze());
        ghost.setCurrentNodeIndex(mazeState.getMaze().getGhostStart().getNodeIndex());
        ghost.setEdibleTime(0);
    }

    private void resetPacMan() {
        pacman.setMaze(mazeState.getMaze());
        pacman.setCurrentNodeIndex(mazeState.getMaze().getPacmanStart().getNodeIndex());
        pacman.setDirection(Direction.LEFT);
    }

    // Score awarded when the ghost is eaten
    private int ghostEatenScore()
    {
        // Ghosts eaten under the same power pill effect
        int ghostsEaten = ghosts.size() - edibleGhostsTotal();
        int score = EAT_GHOST_SCORE;

        // increase the score
        for (int i = 0; i < ghostsEaten; i++)
            score *= 2;

        return score;
    }

//    private void resetNaturalReversalTimer()
//    {
//        mazeTimeSteps = 0;
//
//        for (int i = 0; i < naturalReversalFlags.length; i++) {
//            naturalReversalFlags[i] = false;
//        }
//    }

    // Returns the number of edible ghosts in this specific moment.
    private int edibleGhostsTotal()
    {
        int nEdibleGhosts = 0;

        for (Ghost g : ghosts)
            if (g.isEdible())
                nEdibleGhosts++;

        return nEdibleGhosts;
    }

    private void resetScore() {
        score = 0;
    }

    private void resetLives() {
        lives = START_LIVES;
    }

    public void reset() {
        resetMazes();
        resetLives();
        resetScore();
        resetPacMan();
        resetGhosts();
    }

    public void registerGameStateObserver(GameEventsObserver observer) {
        gameEventsObservers.add(observer);
        notifyGameStateObservers(); // TODO: bug????
    }

    public void notifyGameStateObservers() {
        for (GameEventsObserver observer : gameEventsObservers) {
            observer.gameStatedUpdated(this);
        }
    }

    public GameState copy()
    {
        List<Ghost> newGhosts = new ArrayList<Ghost>();

        for (Ghost ghost : ghosts)
            newGhosts.add(new Ghost(ghost));

        GameState newGameState = new GameState(new MazeState(mazeState), new Pacman(pacman), newGhosts);
        newGameState.score = this.score;
        newGameState.lives = this.lives;
        newGameState.timeSteps = this.timeSteps;
        newGameState.mazeTimeSteps = this.mazeTimeSteps;
        newGameState.extraLifeAwarded = this.extraLifeAwarded;
        newGameState.mazeIndex = this.mazeIndex;
        newGameState.mazes = mazes.clone();
        newGameState.lostLifeOnLastTimeStep = lostLifeOnLastTimeStep;
//        newGameState.naturalReversalFlags = naturalReversalFlags.clone();

        return newGameState;
    }

}
