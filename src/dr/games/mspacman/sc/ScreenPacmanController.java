package dr.games.mspacman.sc;

import dr.games.mspacman.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class ScreenPacmanController implements Runnable {

    // The agent used to move the pacman
    private PacAgent agent;

    // Used to take the screenshots and send keystrokes
    private Robot robot;

    // The width of the screen resolution
    private int screenWidth;

    // The height of the screen resolution
    private int screenHeight;

    // The width of the screenshots taken from the game
    private static final int WIDTH = 224;

    // The height of the screenshots taken from the game
    private static final int HEIGHT = 240;

    // Number of width nodes
    private static final int GAME_COLUMNS = 28;

    // Number of height nodes
    private static final int GAME_ROWS = 30;

    // Size of each node
    private static final int NODE_SIZE = 8;

    private static final int X_PIXEL_OFFSET = 3;

    private static final int SIZE = 2;

    private GameState gameState;

    // The position and size of the rectangle on the screen where screenshots are taken
    private Rectangle rect;

    // Active if it finds the game on the screen
    private boolean gameDetected = false;

    // The next keystroke to be pressed
    private int next = KeyEvent.VK_UP;

    // The current keystroke being pressed
    private int now = KeyEvent.VK_UP;

    // The color of Pac-man
    private static final Color PACMAN_COLOR = new Color(0xffff00);

    // The color of blinky ghost
    private static final Color BLINKY_COLOR = new Color(0xff0000);

    // The color of pinky ghost
    private static final Color PINKY_COLOR = new Color(0xffb8de);

    // The color of sue ghost
    private static final Color SUE_COLOR = new Color(0x00ffde);

    // The color of inky color
    private static final Color INKY_COLOR = new Color(0xffb847);

    // The color of the edible ghosts
    private static final Color EDIBLE_GHOST_COLOR = new Color(0x2121de);

    // The color of the edible ghosts at the end of the edible time
    private static final Color EDIBLE_GHOST_COLOR_BLINK = new Color(0xdedede);

    // Last screenshot of the game
    private BufferedImage gameScreen;

    // Array of pixels of the last screenshot
    private int[] gamePixels;

    // Maps a pixel in the image array to a pill node in the gameState.
    private Map<Integer, Node> pixelsPillNodes = new HashMap<Integer, Node>();

    // Maps a pixel in the image array to a power pill node in the gameState.
    private Map<Integer, Node> pixelsPowerPillNodes = new HashMap<Integer, Node>();

    // Maps a pixel in the image array to a node in the gameState.
    private Map<Integer, Node> pixelsNodes = new HashMap<Integer, Node>();

    // X and Y position of pacman. This is the x and y position of the centre of pacman in the game image
    private Point pacmanPoint;

    private Point blinkyPoint;

    private Point pinkyPoint;

    private Point inkyPoint;

    private Point suePoint;

    private List<Point> ghostsPoints = new ArrayList<Point>();

    private Ghost blinkyGhost;

    private Ghost pinkyGhost;

    private Ghost inkyGhost;

    private Ghost sueGhost;

    private int powerPillsCounter[] = new int[4];

    private int blinkyCounter = 0;

    private int pinkyCounter = 0;

    private int inkyCounter = 0;

    private int sueCounter = 0;

    public ScreenPacmanController(PacAgent agent, GameState gameState)
    {
        this.agent = agent;
        this.gameState = gameState;

//        for (Ghost ghost : gameState.getGhosts())
//        {
//            if (ghost.getType() == GhostType.BLINKY) {
//                blinkyGhost = ghost;
//            } else if (ghost.getType() == GhostType.PINKY) {
//                pinkyGhost = ghost;
//            } else if (ghost.getType() == GhostType.INKY) {
//                inkyGhost = ghost;
//            } else if (ghost.getType() == GhostType.SUE) {
//                sueGhost = ghost;
//            }
//        }

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        detectScreenSize();
        locateGame();
        captureGame();
        createNodesMaps();
    }

    // Captures and saves the game screenshot. It takes the screenshot
    // of only the relevant part of the game. After the game was detected
    // it will continue to take screnshots of only this part.
    public void captureGame() {
        gameScreen = robot.createScreenCapture(rect);
        gamePixels = gameScreen.getRGB(0, 0, gameScreen.getWidth(),
                gameScreen.getHeight(), null, 0, gameScreen.getWidth());
    }

     // a connected object is a set of points on the screen that have the
     // same color. This connected set can be analysed by the number of points to
     // determine if it was a mobile object (pacman, ghosts, etc) or not
     // The algorithm used here is the flood fill algorithm
    public ConnectedObject createConnectedPoints(int x, int y, int targetColor,
                                                 int replaceColor, ConnectedObject cp) {
        if (x >= WIDTH || x < 0 || y >= HEIGHT || y < 0) {
            return cp;
        }
        if (gameScreen.getRGB(x, y) != targetColor) {
            return cp;
        }
        if (gameScreen.getRGB(x, y) == replaceColor) {
            return cp;
        }
        gameScreen.setRGB(x, y, replaceColor);
        cp.addPoint(new Point(x, y));
        createConnectedPoints(x - 1, y, targetColor, replaceColor, cp);
        createConnectedPoints(x + 1, y, targetColor, replaceColor, cp);
        createConnectedPoints(x, y - 1, targetColor, replaceColor, cp);
        createConnectedPoints(x, y + 1, targetColor, replaceColor, cp);
        return cp;
    }

    public ConnectedObject createConnectedObject(int x, int y, int color) {
        ConnectedObject cp = new ConnectedObject();
        createConnectedPoints(x, y, color, Color.GREEN.getRGB(), cp);
        return cp;
    }

    // Puts the nodes in maps for convenience
    private void createNodesMaps()
    {
        Maze maze = gameState.getMazeState().getMaze();

        for (Node node : maze.getNodes()) {
            pixelsNodes.put(nodeToPixelIndex(node), node);
        }

        for (Node node : maze.getPillsNodes()) {
            pixelsPillNodes.put(nodeToPixelIndex(node), node);
        }

        for (Node node : maze.getPowerPillsNodes()) {
            pixelsPowerPillNodes.put(nodeToPixelIndex(node), node);
        }
    }

    private void delay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void detectCharacters() {
//        ghostsPoints.clear();
//        if (blinkyCounter > 5) {
//            blinkyPoint = null;
//        }
//        if (pinkyCounter > 5) {
//            pinkyPoint = null;
//        }
//        if (inkyCounter > 5) {
//            inkyPoint = null;
//        }
//        if (sueCounter > 5) {
//            suePoint = null;
//        }
        for (int i = 0; i < gamePixels.length; i++)
        {
            int color = gamePixels[i];
            int x = i % WIDTH;
            int y = i / WIDTH;

            if (gamePixels[i] == PACMAN_COLOR.getRGB()) {
                ConnectedObject cp = createConnectedObject(x, y, color);
                if (cp.getPoints().size() > 60) {
                    pacmanPoint = pixelPointToNodePoint(cp.centerOfPoints());
                }
            }
//            else if (color == PINKY_COLOR.getRGB()) {
//                ConnectedObject cp = createConnectedObject(x, y, color);
//                if (cp.getPoints().size() > 80) {
//                    pinkyPoint = pixelPointToNodePoint(cp.centerOfPoints());
//                    pinkyCounter = 0;
//                }
//            } else if (color == INKY_COLOR.getRGB()) {
//                ConnectedObject cp = createConnectedObject(x, y, color);
//                if (cp.getPoints().size() > 80) {
//                    inkyPoint = pixelPointToNodePoint(cp.centerOfPoints());
//                    inkyCounter = 0;
//                }
//            } else if (color == SUE_COLOR.getRGB()) {
//                ConnectedObject cp = createConnectedObject(x, y, color);
//                if (cp.getPoints().size() > 80) {
//                    suePoint = pixelPointToNodePoint(cp.centerOfPoints());
//                    sueCounter = 0;
//                }
//            } else if (color == EDIBLE_GHOST_COLOR.getRGB()) {
//                ConnectedObject cp = createConnectedObject(x, y, color);
//                if (cp.getPoints().size() > 80) {
//                    ghostsPoints.add(pixelPointToNodePoint(cp.centerOfPoints()));
//                }
//            } else if (color == EDIBLE_GHOST_COLOR_BLINK.getRGB()) {
//                ConnectedObject cp = createConnectedObject(x, y, color);
//                if (cp.getPoints().size() > 80) {
//                    ghostsPoints.add(pixelPointToNodePoint(cp.centerOfPoints()));
//                }
//            }
        }

//        for (Map.Entry<Integer, Node> entry : pixelsNodes.entrySet()) {
//            Node node = entry.getValue();
//            Point point = nodeToPixelPoint(node);
//            int color = gamePixels[entry.getKey()];
//            if (color == BLINKY_COLOR.getRGB()) {
//                ConnectedObject cp = createConnectedObject(point.x, point.y, color);
//                if (cp.getPoints().size() > 80) {
//                    blinkyPoint = pixelPointToNodePoint(cp.centerOfPoints());
//                    blinkyCounter = 0;
//                }
//            }
//        }

//        blinkyCounter++;
//        pinkyCounter++;
//        inkyCounter++;
//        sueCounter++;
    }

    private Maze[] mazes = {Maze.A, Maze. B};

    // Detects the current maze
    private void detectMaze(int[] pixels)
    {
        BufferedImage screenshot = robot.createScreenCapture(new Rectangle(screenWidth, screenHeight));
        int[] screenPixels = screenshot.getRGB(0, 0, screenWidth, screenHeight, null, 0, screenWidth);

        for (int i = 0; i < pixels.length; i++)
        {
            for (Maze maze : mazes)
            {
                if (maze.BORDER_COLOR.getRGB() == screenPixels[i])
                {
                    if (gameState.getMazeState() == null) {
                        MazeState mazeState = new MazeState();
                        mazeState.setMaze(maze);
                        gameState.setMazeState(mazeState);
                    }

                    int gameX = i % screenWidth - 4;
                    int gameY = i / screenWidth + 3;
                    rect = new Rectangle(gameX, gameY, GAME_COLUMNS * NODE_SIZE, GAME_ROWS * NODE_SIZE);
                    gameDetected = true;

                    return;
                }
            }
        }
    }

    private void detectPills()
    {
        MazeState mazeState = gameState.getMazeState();

        for (Map.Entry<Integer, Node> entry : pixelsPillNodes.entrySet())
        {
            Node node = entry.getValue();

            if (gamePixels[entry.getKey()] == mazeState.getMaze().PILL_COLOR.getRGB()) {
                mazeState.getPillsBitSet().set(node.getPillIndex(), true);
            } else {
                mazeState.getPillsBitSet().set(node.getPillIndex(), false);
            }
        }

//        int i = 0;
//
        for (Map.Entry<Integer, Node> entry : pixelsPowerPillNodes.entrySet())
        {
            Node node = entry.getValue();

            if (gamePixels[entry.getKey()] == mazeState.getMaze().PILL_COLOR.getRGB()) {
                mazeState.getPowersBitSet().set(node.getPowerIndex(), true);
            } else {
                mazeState.getPowersBitSet().set(node.getPowerIndex(), false);
            }

//            if (gamePixels[entry.getKey()] == m.getPowerPillsColor().getRGB()) {
//                node.setPillType(PillType.POWER_PILL);
//                powerPillsCounter[i] = 0;
//            } else {
//                powerPillsCounter[i]++;
//                if (powerPillsCounter[i] > 20) {
//                    node.setPillType(PillType.EMPTY);
//                }
//            }
//            i++;
        }
    }

    public void detectScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = (int) screenSize.getWidth();
        screenHeight = (int) screenSize.getHeight();
        System.out.println("Screen size detected: " + screenWidth + " x " + screenHeight);
    }

    public int directionToKeyEvent(Direction direction) {
        if (direction == Direction.UP)
            return KeyEvent.VK_UP;
        else if (direction == Direction.DOWN)
            return KeyEvent.VK_DOWN;
        else if (direction == Direction.RIGHT)
            return KeyEvent.VK_RIGHT;
        else if (direction == Direction.LEFT)
            return KeyEvent.VK_LEFT;
        return now;
    }

    // Detects the game on the screen and stores the coordinates
    public void locateGame() {
        int[] screenPixels;
        BufferedImage screenshot;
        System.out.print("Detecting game...");
        while (!gameDetected) {
            screenshot = robot.createScreenCapture(new Rectangle(screenWidth, screenHeight));
            screenPixels = screenshot.getRGB(0, 0, screenWidth, screenHeight, null, 0, screenWidth);
            detectMaze(screenPixels);
            delay(20);
        }
        System.out.print("game detected!\n");
    }

    // Returns the index in the pixels array of a node in the gameState
    public int nodeToPixelIndex(Node node) {
        Point p = nodeToPixelPoint(node);
        return ((p.y * WIDTH) + p.x);
    }

    // Returns a pixel point from the given node
    public Point nodeToPixelPoint(Node node) {
        int x = node.getX() * SIZE + X_PIXEL_OFFSET;
        int y = node.getY() * SIZE;
        return new Point(x, y);
    }


    public Point pixelPointToNodePoint(Point pixelPoint) {
        return new Point((pixelPoint.x / 2), (pixelPoint.y / 2));
    }

    public void run() {
        for (int i = 0; i < 1000000000; i++) {
//            System.out.println(i);
            captureGame();
            detectCharacters();
            setCharacters();
            detectPills();
//            gameState.
            gameState.notifyGameStateObservers();
//            if (i > 5) {
                sendKeystrokes();
//            }
            if (i % 50 == 0) {
//                int[] screenPixels;
//                BufferedImage screenshot;
//                screenshot = robot.createScreenCapture(new Rectangle(screenWidth, screenHeight));
//                screenPixels = screenshot.getRGB(0, 0, screenWidth, screenHeight, null, 0, screenWidth);
//                detectMaze(screenPixels);
//                detectMazeChange();
//                saveGameScreen();
            }

//            saveGameScreen();

//            delay(1000);
        }
    }

    private void saveGameScreen()
    {
        Graphics2D g = gameScreen.createGraphics();

        // Print every pixel of the maze nodes
        for (Node node : gameState.getMazeState().getMaze().getNodes()) {
            Point point = nodeToPixelPoint(node);
            g.setColor(Color.MAGENTA);
            g.drawRect(point.x, point.y, 0, 0);
            g.setColor(Color.BLUE);
            g.drawRect(point.x - 4, point.y, 0, 0);
        }
        try {
            ImageIO.write(gameScreen, "png", new File("/Users/drobles/Desktop/createConnectedObject.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendKeystrokes() {
        Direction direction = agent.direction(gameState);
        next = directionToKeyEvent(direction);
        if (now != next) {
            gameState.getPacman().setDirection(direction);
            robot.keyRelease(now);
            robot.keyPress(next); // CHANGES
            now = next;
        }

    }

    private void setCharacters() {
//        List<Ghost> ghs = new ArrayList<Ghost>();
//        ghs.add(blinkyGhost);
//        ghs.add(pinkyGhost);
//        ghs.add(inkyGhost);
//        ghs.add(sueGhost);
//        blinkyGhost.setCurrentNode(null);
//        pinkyGhost.setCurrentNode(null);
//        inkyGhost.setCurrentNode(null);
//        sueGhost.setCurrentNode(null);
//        blinkyGhost.setEdibleTime(100);
//        pinkyGhost.setEdibleTime(100);
//        inkyGhost.setEdibleTime(100);
//        sueGhost.setEdibleTime(100);

        for (Node node : gameState.getMazeState().getMaze().getNodes()) {
            if (pacmanPoint != null) {
                Pacman pacman = new Pacman();
//                if (gameState.getPacman().getDirection() == Direction.LEFT) {
                    if (Math.abs(node.getX() - pacmanPoint.x + 2) < 3
                            && Math.abs(node.getY() - pacmanPoint.y) < 3) {
//                        gameState.getPacman().setCurrentNode(node);
                        pacman.setMaze(Maze.A);
                        pacman.setCurrentNode(node);
                        gameState.setPacman(pacman);
                    }
//                } else {
//                    if (Math.abs(node.getX() - pacmanPoint.x) < 3
//                            && Math.abs(node.getY() - pacmanPoint.y) < 3) {

//                    }
//                }
            }
//            if (blinkyPoint != null) {
//                if (Math.abs(node.getX() - blinkyPoint.x) < 3
//                        && Math.abs(node.getY() - blinkyPoint.y) < 3) {
//                    blinkyGhost.setCurrentNode(node);
//                    ghs.remove(blinkyGhost);
//                }
//            }
//            if (pinkyPoint != null) {
//                if (Math.abs(node.getX() - pinkyPoint.x) < 3
//                        && Math.abs(node.getY() - pinkyPoint.y) < 3) {
//                    pinkyGhost.setCurrentNode(node);
//                    ghs.remove(pinkyGhost);
//                }
//            }
//            if (inkyPoint != null) {
//                if (Math.abs(node.getX() - inkyPoint.x) < 3
//                        && Math.abs(node.getY() - inkyPoint.y) < 3) {
//                    inkyGhost.setCurrentNode(node);
//                    ghs.remove(inkyGhost);
//                }
//            }
//            if (suePoint != null) {
//                if (Math.abs(node.getX() - suePoint.x) < 3
//                        && Math.abs(node.getY() - suePoint.y) < 3) {
//                    sueGhost.setCurrentNode(node);
//                    ghs.remove(sueGhost);
//                }
//            }
//            if (ghostsPoints.size() > 0) {
//                for (Point ghostPoint : ghostsPoints) {
//                    if (Math.abs(node.getX() - ghostPoint.x) < 3
//                            && Math.abs(node.getY() - ghostPoint.y) < 3) {
//                        if (ghs.size() > 0) {
//                            Ghost edibleGhost = ghs.get(0);
//                            edibleGhost.setCurrentNode(node);
//                            edibleGhost.setEdibleTime(100);
//                            ghs.remove(0);
//                        }
//                    }
//                }
//            }
        }
    }

}