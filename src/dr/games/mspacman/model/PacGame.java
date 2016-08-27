package dr.games.mspacman.model;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class PacGame implements KeyListener, Runnable {

    // Game State
    private GameState gameState;

    // Agents
    private PacAgent pacAgent;
    private GhostsAgent ghostsAgent;

//    private boolean historyEnabled = false;
    protected volatile boolean pause = false;
    private boolean running = false;
    private int delay = 0;
    private int currentGameState = 0;
    private List<GameState> gameStates = new ArrayList<GameState>();

    // Observers
    private List<PacGameObserver> gameObservers = new ArrayList<PacGameObserver>();

    public PacGame(PacAgent agent, GhostsAgent ghostsAgent, int delay)
    {
        pacAgent = agent;
        this.ghostsAgent = ghostsAgent;
        this.delay = delay;
        gameState = GameState.newDefaultGameState();
    }

    // Regular methods

    public boolean isPaused() {
        return pause;
    }

    private void delay() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void first() {
        currentGameState = 0;
        notifyTimeStepChange();
    }

    public void fastForward() {
        currentGameState = Math.min(currentGameState + 5, gameStates.size() - 1);
        notifyTimeStepChange();
    }

    public void forward() {
        currentGameState = Math.min(currentGameState + 1, gameStates.size() - 1);
        notifyTimeStepChange();
    }

    public void rewind() {
        currentGameState = Math.max(currentGameState - 1, 0);
        notifyTimeStepChange();
    }

    public void fastRewind() {
        currentGameState = Math.max(currentGameState - 5, 0);
        notifyTimeStepChange();
    }

    public void last() {
        currentGameState = gameStates.size() - 1;
        notifyTimeStepChange();
    }

    public void changeIndex(int index) {
        currentGameState = index;
//        printState();
        notifyTimeStepChange();
    }

    public boolean isRunning() {
        return running;
    }

//    public void printState() {
//        System.out.println(currentGameState + " / " + (gameStates.size() - 1));
//    }

    public void pause() {
        pause = !pause;
//        notifyTimeStepChange();
    }

    public void start() {
//        running = true;
        Thread thread = new Thread(this);
        thread.start();
//        notifyTimeStepChange();
    }

    public void stop() {
        running = false;
        System.exit(0);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void runSimulation()
    {
        while (!gameState.isGameOver())
            playTimeStep();
    }

    public void run()
    {
        running = true;

        while (running) {
            while (!gameState.isGameOver() && !pause) {
                playTimeStep();
    //            notifyTimeStepChange();
                delay();
            }
        }
    }

    public void playTimeStep() {
        gameState.nextState(pacAgent.direction(gameState), ghostsAgent.direction(gameState));
    }    

//    public int getCycles() {
//        return gameStates.size();
//    }

//    public int getCurrentCycle() {
//        return currentGameState;
//    }

//    public GameState getCurrentGameState() {
//        if (historyEnabled) {
//            return gameState;
//        }
//        return gameStates.get(currentGameState);
//    }

    public void notifyTimeStepChange() {
        for (PacGameObserver o : gameObservers)
            o.gameStatedUpdated(gameState);
    }

    public void registerObserver(PacGameObserver observer) {
        gameObservers.add(observer);
    }

    public void registerGameStateObserver(GameEventsObserver gameEventsObserver) {
        gameState.registerGameStateObserver(gameEventsObserver);
    }

    public void play() {
//        last();
        pause = false;
        notifyTimeStepChange();
    }

    public void reset() {
        gameState.reset();
    }

    // KeyListener

    @Override
    public void keyPressed(KeyEvent keyEvent)
    {
        int keyCode = keyEvent.getKeyCode();

        // Pause
        if (keyCode == KeyEvent.VK_P)
            pause();
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) { }

    @Override
    public void keyTyped(KeyEvent keyEvent) { }

}
