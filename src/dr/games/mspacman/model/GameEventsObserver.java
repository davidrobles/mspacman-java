package dr.games.mspacman.model;

public interface GameEventsObserver {

    void gameStatedUpdated(GameState newGameState);

}
