package dr.games.mspacman.agents;

import dr.games.mspacman.model.Direction;
import dr.games.mspacman.model.GameState;
import dr.games.mspacman.model.PacAgent;
import dr.games.mspacman.view.AgentView;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class KeyboardController implements PacAgent, KeyListener, AgentView {

    private Direction desiredDir = Direction.LEFT;
    private GameState gameState;

    // PacAgent

    @Override
    public Direction direction(GameState gameState) {
        this.gameState = gameState;
        return desiredDir;
    }

    // KeyListener

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_RIGHT)
            desiredDir = Direction.RIGHT;
        else if (keyCode == KeyEvent.VK_LEFT)
            desiredDir = Direction.LEFT;
        else if (keyCode == KeyEvent.VK_UP)
            desiredDir = Direction.UP;
        else if (keyCode == KeyEvent.VK_DOWN)
            desiredDir = Direction.DOWN;
    }

    @Override
    public void keyTyped(KeyEvent event) { }

    @Override
    public void keyReleased(KeyEvent event) { }

    @Override
    public void drawUnderPills(Graphics2D dbg2D, int startX, int startY, int nodeSize) {
        
    }

    @Override
    public void drawAbovePills(Graphics2D dbg2D) {

    }

    @Override
    public void drawAboveEverything(Graphics2D dbg2D) {
//        dbg2D.setColor(Color.GREEN);
//        dbg2D.drawString(desiredDir.toString(), 120, 285);
    }

}

