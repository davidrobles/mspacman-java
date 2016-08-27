package dr.games.mspacman.view;

import dr.games.mspacman.model.Direction;
import dr.games.mspacman.model.*;
import dr.games.mspacman.model.Pacman;
import dr.games.mspacman.model.Maze;
import dr.games.mspacman.util.PacUtil;
import dr.util.FrameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class GameStateView extends JPanel implements MouseListener, GameEventsObserver
{
    // Game State
    private GameState gameState;
    private MazeState mazeState;
    private Pacman pacman;
    private List<Ghost> ghosts;

    // Caching
    private Maze currentMaze;

    // Drawing
    protected Graphics2D dbg2D;
    protected Image dbImage = null;
    protected Image mazeImage = null;

    private List<AgentView> extras = new ArrayList<AgentView>();

    // Dimensions
    public static final int SCALE = 2;
    public static final int MENU_HEIGHT = 10 * SCALE;
    public static final int STATUS_BAR_HEIGHT = 20 * SCALE;
    public static final int GAME_HEIGHT = 121 * SCALE + 6; // TODO: LAZY CODE FIX 121
    protected static final int PWIDTH = 109 * SCALE + 6; // TODO: LAZY CODE FIX 109
    protected static final int PHEIGHT = MENU_HEIGHT + GAME_HEIGHT + STATUS_BAR_HEIGHT;

    /** The size for the base of the joystick */
    public static final int JOYSTICK_BASE_SIZE = (int) (STATUS_BAR_HEIGHT * .8);

    /** The size for the actual joystick */
    public static final int JOYSTICK_SIZE = JOYSTICK_BASE_SIZE / 3;

    /** The size of a mobile object (height and width) */
    public static final int MOBILE_SIZE = 5;

    // Colours
    protected static final Color PILL_COLOR = Color.WHITE;
    protected static final Color POWER_PILL_COLOR = Color.WHITE;
    private static final Color STATUS_BAR_BG_COLOR = Color.BLACK;

    // Text
    private static final Font menuFont = new Font("sansserif", Font.BOLD, 5 * SCALE);
    private static final NumberFormat nf = NumberFormat.getInstance();

    // Images
    BufferedImage[][] pacmanImgs = new BufferedImage[4][3];
    BufferedImage[][][] ghostsImgs = new BufferedImage[6][4][2];

    public GameStateView() {
        setBackground(Color.BLACK);
        loadSprites();
        addMouseListener(this);
    }

    public static GameStateView newGameStateViewFramed()
    {
        GameStateView view = new GameStateView();
        JFrame frame = new JFrame();
        frame.add(view);
        frame.pack();
        FrameUtils.centerFrame(frame);
        frame.setVisible(true);
        
        return view;
    }

    private void loadSprites()
    {
        // PACMAN

        // Up
        pacmanImgs[Direction.UP.ordinal()][0] = PacUtil.getImage("mspacman-up-normal.png");
        pacmanImgs[Direction.UP.ordinal()][1] = PacUtil.getImage("mspacman-up-open.png");
        pacmanImgs[Direction.UP.ordinal()][2] = PacUtil.getImage("mspacman-up-closed.png");

        // Right
        pacmanImgs[Direction.RIGHT.ordinal()][0] = PacUtil.getImage("mspacman-right-normal.png");
        pacmanImgs[Direction.RIGHT.ordinal()][1] = PacUtil.getImage("mspacman-right-open.png");
        pacmanImgs[Direction.RIGHT.ordinal()][2] = PacUtil.getImage("mspacman-right-closed.png");

        // Down
        pacmanImgs[Direction.DOWN.ordinal()][0] = PacUtil.getImage("mspacman-down-normal.png");
        pacmanImgs[Direction.DOWN.ordinal()][1] = PacUtil.getImage("mspacman-down-open.png");
        pacmanImgs[Direction.DOWN.ordinal()][2] = PacUtil.getImage("mspacman-down-closed.png");

        // Left
        pacmanImgs[Direction.LEFT.ordinal()][0] = PacUtil.getImage("mspacman-left-normal.png");
        pacmanImgs[Direction.LEFT.ordinal()][1] = PacUtil.getImage("mspacman-left-open.png");
        pacmanImgs[Direction.LEFT.ordinal()][2] = PacUtil.getImage("mspacman-left-closed.png");

        // BLINKY

        // Up
        ghostsImgs[0][Direction.UP.ordinal()][0] = PacUtil.getImage("blinky-up-1.png");
        ghostsImgs[0][Direction.UP.ordinal()][1] = PacUtil.getImage("blinky-up-2.png");

        // Right
        ghostsImgs[0][Direction.RIGHT.ordinal()][0] = PacUtil.getImage("blinky-right-1.png");
        ghostsImgs[0][Direction.RIGHT.ordinal()][1] = PacUtil.getImage("blinky-right-2.png");

        // Down
        ghostsImgs[0][Direction.DOWN.ordinal()][0] = PacUtil.getImage("blinky-down-1.png");
        ghostsImgs[0][Direction.DOWN.ordinal()][1] = PacUtil.getImage("blinky-down-2.png");

        // Left
        ghostsImgs[0][Direction.LEFT.ordinal()][0] = PacUtil.getImage("blinky-left-1.png");
        ghostsImgs[0][Direction.LEFT.ordinal()][1] = PacUtil.getImage("blinky-left-2.png");

        // INKY

        // Up
        ghostsImgs[1][Direction.UP.ordinal()][0] = PacUtil.getImage("pinky-up-1.png");
        ghostsImgs[1][Direction.UP.ordinal()][1] = PacUtil.getImage("pinky-up-2.png");

        // Right
        ghostsImgs[1][Direction.RIGHT.ordinal()][0] = PacUtil.getImage("pinky-right-1.png");
        ghostsImgs[1][Direction.RIGHT.ordinal()][1] = PacUtil.getImage("pinky-right-2.png");

        // Down
        ghostsImgs[1][Direction.DOWN.ordinal()][0] = PacUtil.getImage("pinky-down-1.png");
        ghostsImgs[1][Direction.DOWN.ordinal()][1] = PacUtil.getImage("pinky-down-2.png");

        // Left
        ghostsImgs[1][Direction.LEFT.ordinal()][0] = PacUtil.getImage("pinky-left-1.png");
        ghostsImgs[1][Direction.LEFT.ordinal()][1] = PacUtil.getImage("pinky-left-2.png");

        // INKY

        // Up
        ghostsImgs[2][Direction.UP.ordinal()][0] = PacUtil.getImage("inky-up-1.png");
        ghostsImgs[2][Direction.UP.ordinal()][1] = PacUtil.getImage("inky-up-2.png");

        // Right
        ghostsImgs[2][Direction.RIGHT.ordinal()][0] = PacUtil.getImage("inky-right-1.png");
        ghostsImgs[2][Direction.RIGHT.ordinal()][1] = PacUtil.getImage("inky-right-2.png");

        // Down
        ghostsImgs[2][Direction.DOWN.ordinal()][0] = PacUtil.getImage("inky-down-1.png");
        ghostsImgs[2][Direction.DOWN.ordinal()][1] = PacUtil.getImage("inky-down-2.png");

        // Left
        ghostsImgs[2][Direction.LEFT.ordinal()][0] = PacUtil.getImage("inky-left-1.png");
        ghostsImgs[2][Direction.LEFT.ordinal()][1] = PacUtil.getImage("inky-left-2.png");

        // SUE

        // Up
        ghostsImgs[3][Direction.UP.ordinal()][0] = PacUtil.getImage("sue-up-1.png");
        ghostsImgs[3][Direction.UP.ordinal()][1] = PacUtil.getImage("sue-up-2.png");

        // Right
        ghostsImgs[3][Direction.RIGHT.ordinal()][0] = PacUtil.getImage("sue-right-1.png");
        ghostsImgs[3][Direction.RIGHT.ordinal()][1] = PacUtil.getImage("sue-right-2.png");

        // Down
        ghostsImgs[3][Direction.DOWN.ordinal()][0] = PacUtil.getImage("sue-down-1.png");
        ghostsImgs[3][Direction.DOWN.ordinal()][1] = PacUtil.getImage("sue-down-2.png");

        // Left
        ghostsImgs[3][Direction.LEFT.ordinal()][0] = PacUtil.getImage("sue-left-1.png");
        ghostsImgs[3][Direction.LEFT.ordinal()][1] = PacUtil.getImage("sue-left-2.png");

        // EDIBLE GHOST

        ghostsImgs[4][0][0] = PacUtil.getImage("edible-ghost-1.png");
        ghostsImgs[4][0][1] = PacUtil.getImage("edible-ghost-2.png");

        // EDIBLE GHOST BLINKING

        ghostsImgs[5][0][0] = PacUtil.getImage("edible-ghost-blink-1.png");
        ghostsImgs[5][0][1] = PacUtil.getImage("edible-ghost-blink-2.png");
    }

    // Loads the image. For caching purposes.
    private void renderMaze()
    {
        // Load the maze image for the first time, and on a maze change
        if (currentMaze == null || currentMaze != mazeState.getMaze())
        {
            currentMaze = mazeState.getMaze();

            if (currentMaze == Maze.A)
                mazeImage = PacUtil.getImage("maze-a.png");
            else if (currentMaze == Maze.B)
                mazeImage = PacUtil.getImage("maze-b.png");
            else if (currentMaze == Maze.C)
                mazeImage = PacUtil.getImage("maze-c.png");
            else if (currentMaze == Maze.D)
                mazeImage = PacUtil.getImage("maze-d.png");
        }
    }

    private void drawMaze() {
        renderMaze();
        dbg2D.drawImage(mazeImage, 0, MENU_HEIGHT, null);

    }

    private void drawMenu() {
        dbg2D.setColor(Color.BLACK);
        dbg2D.fillRect(0, 0, PWIDTH, MENU_HEIGHT);
    }

    private void drawMenuData() {
        dbg2D.setColor(Color.WHITE);
        dbg2D.drawString("Score: ", 5, SCALE * 7);
        dbg2D.drawString(nf.format(gameState.getScore()), 24 * SCALE, SCALE * 7);
    }

    protected void drawPills()
    {
        // Draw Pills
        dbg2D.setColor(PILL_COLOR);

        for (int i = 0; i < mazeState.getMaze().getPillsNodes().size(); i++)
        {
            // if the pill node contains a pill
            if (mazeState.getPillsBitSet().get(i)) {
                int x = mazeState.getMaze().getPillsNodes().get(i).getX() * SCALE + 2;
                int y = (mazeState.getMaze().getPillsNodes().get(i).getY() * SCALE) + MENU_HEIGHT + 2;
                dbg2D.fillRect(x, y, SCALE, SCALE);
            }
        }

        // Draw Power Pills
        dbg2D.setColor(POWER_PILL_COLOR);

        for (int i = 0; i < mazeState.getMaze().getPowerPillsNodes().size(); i++)
        {
            // if the pill node contains a pill
            if (mazeState.getPowersBitSet().get(i)) {
                int x = (mazeState.getMaze().getPowerPillsNodes().get(i).getX() * SCALE);
                int y = MENU_HEIGHT + (mazeState.getMaze().getPowerPillsNodes().get(i).getY() * SCALE);
                dbg2D.fillOval(x, y, SCALE * 3, SCALE * 3);
            }
        }
    }

    private void drawPacMan()
    {
        if (pacman != null)
        {
            // Get Pac-Man current position and direction
            int x = pacman.getCurrentNode().getX();
            int y = pacman.getCurrentNode().getY();
            Direction curDir = pacman.getDirection();

            // Get current image indexes
            int pacImgDir = curDir.ordinal();
            int pacImgSeq = ((curDir == Direction.UP || curDir == Direction.DOWN ? y : x) % 6) / 2;

            // Draw Pac-Man image
            dbg2D.drawImage(pacmanImgs[pacImgDir][pacImgSeq], x * SCALE - (SCALE * MOBILE_SIZE / 2) + 2,
                    y * SCALE - (SCALE * MOBILE_SIZE / 2) + MENU_HEIGHT + 2, null);
        }
    }

    private void drawGhosts()
    {
        if (ghosts != null)
        {
            for (Ghost g : ghosts) {
                // Get Pac-Man current position and direction
                int x = g.getCurrentNode().getX();
                int y = g.getCurrentNode().getY();
                Direction curDir = g.getDirection();

                // Normal state
                if (!g.isEdible()) {
                    dbg2D.drawImage(ghostsImgs[g.getType().ordinal()][curDir.ordinal()]
                            [(gameState.getTimeSteps() % 6) / 3], x * SCALE - (SCALE * MOBILE_SIZE / 2) + 2,
                            (y * SCALE - (SCALE * MOBILE_SIZE / 2)) + MENU_HEIGHT, null);
                }

                // Blue state
                else {
                    if (g.getEdibleTime() < 40 && ((gameState.getTimeSteps() % 6 / 3) == 0))
                        dbg2D.drawImage(ghostsImgs[5][0][(gameState.getTimeSteps() % 6) / 3],
                                x * SCALE - (SCALE * MOBILE_SIZE / 2) + 2, (y * SCALE - (SCALE * MOBILE_SIZE / 2))
                                        + MENU_HEIGHT, null);
                    else
                        dbg2D.drawImage(ghostsImgs[4][0][(gameState.getTimeSteps() % 6) / 3],
                                x * SCALE - (SCALE * MOBILE_SIZE / 2) + 2, (y * SCALE - (SCALE * MOBILE_SIZE / 2))
                                        + MENU_HEIGHT, null);
                }
            }
        }
    }




    private void drawJoystick() {
                // Draw Joystick
                dbg2D.setColor(Color.LIGHT_GRAY);
                dbg2D.fillOval((PWIDTH / 2) - (JOYSTICK_BASE_SIZE / 2), MENU_HEIGHT
                        + GAME_HEIGHT + ((STATUS_BAR_HEIGHT - JOYSTICK_BASE_SIZE) / 2), JOYSTICK_BASE_SIZE,
                        JOYSTICK_BASE_SIZE);
    //            if (((KeyboardController)model.getPacman().getController()).desDir != null) {
    //                switch (((KeyboardController)model.getPacman().getController()).desDir) {
//                if (model.getPacman().getController().direction(model, model.getPacman()) != null) {
                    switch (gameState.getPacman().getDirection()) {
//                        case CENTER:
//                            dbg2D.setColor(Color.RED);
//                            dbg2D.fillOval((PWIDTH / 2) - (JOYSTICK_SIZE / 2), MENU_HEIGHT + GAME_HEIGHT
//                                    + (STATUS_BAR_HEIGHT / 2) - (JOYSTICK_SIZE / 2), JOYSTICK_SIZE,
//                                    JOYSTICK_SIZE);
//                            break;
                        case UP:
                            dbg2D.setColor(Color.GRAY);
                            dbg2D.drawLine(PWIDTH / 2, MENU_HEIGHT + GAME_HEIGHT + (STATUS_BAR_HEIGHT / 2)
                                    - (JOYSTICK_BASE_SIZE / 2), PWIDTH / 2, MENU_HEIGHT + GAME_HEIGHT
                                    + (STATUS_BAR_HEIGHT / 2));
                            dbg2D.setColor(Color.RED);
                            dbg2D.fillOval((PWIDTH / 2) - (JOYSTICK_SIZE / 2), MENU_HEIGHT + GAME_HEIGHT
                                    + (STATUS_BAR_HEIGHT / 2) - (JOYSTICK_BASE_SIZE / 2), JOYSTICK_SIZE,
                                    JOYSTICK_SIZE);
                            break;
                        case RIGHT:
                            dbg2D.setColor(Color.GRAY);
                            dbg2D.drawLine(PWIDTH / 2, MENU_HEIGHT + GAME_HEIGHT
                                    + (STATUS_BAR_HEIGHT / 2), (PWIDTH / 2) + (JOYSTICK_BASE_SIZE / 2) -
                                    (JOYSTICK_SIZE / 2), MENU_HEIGHT + GAME_HEIGHT + (STATUS_BAR_HEIGHT / 2));
                            dbg2D.setColor(Color.RED);
                            dbg2D.fillOval((PWIDTH / 2) + (JOYSTICK_BASE_SIZE / 2) - JOYSTICK_SIZE, MENU_HEIGHT
                                    + GAME_HEIGHT + (STATUS_BAR_HEIGHT / 2) - (JOYSTICK_SIZE / 2),
                                    JOYSTICK_SIZE, JOYSTICK_SIZE);
                            break;
                        case DOWN:
                            dbg2D.setColor(Color.GRAY);
                            dbg2D.drawLine(PWIDTH / 2, MENU_HEIGHT + GAME_HEIGHT
                                    + (STATUS_BAR_HEIGHT / 2), PWIDTH / 2, MENU_HEIGHT + GAME_HEIGHT
                                    + (STATUS_BAR_HEIGHT / 2) + (JOYSTICK_BASE_SIZE / 2) - (JOYSTICK_SIZE / 2));
                            dbg2D.setColor(Color.RED);
                            dbg2D.fillOval((PWIDTH / 2) - (JOYSTICK_SIZE / 2), MENU_HEIGHT + GAME_HEIGHT
                                    + (STATUS_BAR_HEIGHT / 2) + (JOYSTICK_BASE_SIZE / 2) - JOYSTICK_SIZE,
                                    JOYSTICK_SIZE, JOYSTICK_SIZE);
                            break;
                        case LEFT:
                            dbg2D.setColor(Color.GRAY);
                            dbg2D.drawLine(PWIDTH / 2, MENU_HEIGHT + GAME_HEIGHT
                                    + (STATUS_BAR_HEIGHT / 2), (PWIDTH / 2) - (JOYSTICK_BASE_SIZE / 2),
                                    MENU_HEIGHT
                                    + GAME_HEIGHT + (STATUS_BAR_HEIGHT / 2));
                            dbg2D.setColor(Color.RED);
                            dbg2D.fillOval((PWIDTH / 2) - (JOYSTICK_BASE_SIZE / 2), MENU_HEIGHT + GAME_HEIGHT
                                    + (STATUS_BAR_HEIGHT / 2) - (JOYSTICK_SIZE / 2), JOYSTICK_SIZE,
                                    JOYSTICK_SIZE);
                            break;
                    }
//                }
    }


    private void drawAboveEverything() {
        for (AgentView e : extras) {
            e.drawAboveEverything(dbg2D);
        }
    }

    private void drawUnderPills() {
        for (AgentView e : extras) {
            e.drawUnderPills(dbg2D, 2, MENU_HEIGHT, SCALE);
        }
    }

    private void drawAbovePills() {
        for (AgentView e : extras) {
            e.drawAbovePills(dbg2D);
        }
    }

    private void drawLines() {
        dbg2D.setColor(Color.GREEN);
        for (Node node : mazeState.getMaze().getNodes()) {
            for (Node neighbor : node.getNeighbors()) {
                dbg2D.drawLine(node.getX() * SCALE + 2, node.getY() * SCALE + MENU_HEIGHT + 2,
                        neighbor.getX() * SCALE + 2, neighbor.getY() * SCALE + MENU_HEIGHT + 2);
            }
        }
    }


    private void drawLives()
    {
        for (int i = 0; i < gameState.getLives(); i++) {
            dbg2D.drawImage(pacmanImgs[Direction.RIGHT.ordinal()][0], 10 + (i * 20),
                    10 + GAME_HEIGHT + MENU_HEIGHT, null);
        }
    }

    protected void drawStatusBar() {
        dbg2D.setColor(STATUS_BAR_BG_COLOR);
        dbg2D.fillRect(0, MENU_HEIGHT + GAME_HEIGHT, PWIDTH, STATUS_BAR_HEIGHT);
    }

    public void gameRender()
    {
        if (dbg2D != null) {
            drawMaze();
            drawMenu();
            drawMenuData();
            drawUnderPills();
            drawPills();
            drawPacMan();
            drawGhosts();
            drawStatusBar();
            drawLives();
            drawAboveEverything();
    //        drawJoystick();
    //        drawLines();
            // TODO: temp saving
//            try {
//                ImageIO.write((RenderedImage) dbImage, "jpg", new File("/Users/drobles/Desktop/last/last"
//                        + gameState.getTimeSteps() + ".jpg"));
//            } catch (IOException ignored) { }
        }
    }


    protected void screenUpdate() {
        Graphics g = this.getGraphics();
        if (dbImage != null)
            g.drawImage(dbImage, 0, 0, null);
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void addExtras(AgentView extra) {
        extras.add(extra);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Setup graphics context
        if (dbImage == null) {
            dbImage = createImage(PWIDTH, PHEIGHT);
            dbg2D = (Graphics2D) dbImage.getGraphics();
            dbg2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            dbg2D.setFont(menuFont);
        }
        if (gameState != null) {
            gameRender();
            screenUpdate();
        } else {
            dbg2D.setColor(Color.RED);
            dbg2D.fillRect(0, 0, 50, 50);
        }
    }

    // Game State Observer

    @Override
    public void gameStatedUpdated(GameState newGameState) {
        this.gameState = newGameState;
        this.mazeState = newGameState.getMazeState();
        this.pacman = newGameState.getPacman();
        this.ghosts = newGameState.getGhosts();
        gameRender();
        screenUpdate();
    }

    // Mouse listener

    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PWIDTH, PHEIGHT);
    }


}
