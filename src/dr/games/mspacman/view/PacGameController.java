package dr.games.mspacman.view;

import dr.games.mspacman.model.PacGame;

public class PacGameController {

    private PacGame game;

    public PacGameController(PacGame game) {
        this.game = game;
    }

    public void startStop() {
        if (game.isRunning())
            game.stop();
        else
            game.start();
    }

    public void first() {
        game.first();
    }

    // <<
    public void fastRewind() {
        game.fastRewind();
    }

    // <
    public void rewind() {
        game.rewind();
    }

    // Play/Pause
    public void playPause() {
        if (game.isPaused())
            game.play();
        else
            game.pause();
    }

    // >
    public void forward() {
        game.forward();
    }

    // >>
    public void fastForward() {
        game.fastForward();
    }

    public void last() {
        game.last();
    }

    public void changeIndex(int index) {
        game.changeIndex(index);
    }

}
