package dr.games.mspacman.model;

import dr.games.mspacman.util.PacUtil;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public enum Maze {

    A("A.txt"), B("B.txt"), C("C.txt"), D("D.txt");

    public int cols;
    public int rows;
    private Node[][] maze;
    private List<Node> nodes = new ArrayList<Node>();
    private List<Node> powerPillNodes = new ArrayList<Node>();
    private List<Node> pillNodes = new ArrayList<Node>();
    private int[][] nodeDistances;
    private Node ghostStart;
    private Node pacmanStart;
    public Color BORDER_COLOR = new Color(0xffb897);
    public Color PILL_COLOR = new Color(0xdedede);
    public Color POWER_PILL_COLOR = new Color(0xdedede);

    Maze(String fileName)
    {
        loadMap(fileName);
        nodeDistances = new int[nodes.size()][nodes.size()];
        setNeighbors();
        setDistances();
    }

    private void loadMap(String fileName)
    {
        File file = new File("MsPacMan/maps/" + fileName);

        try {
            Scanner scanner = new Scanner(file);
            String meta = scanner.nextLine();
            String[] dimension = meta.split(",");
            cols = Integer.parseInt(dimension[0]);
            rows = Integer.parseInt(dimension[1]);
            maze = new Node[cols][rows];
            int y = 0;
            int nodeCounter = 0;
            int pillsCounter = 0;
            int powersCounter = 0;

            // for every line of the map text file
            while (scanner.hasNextLine())
            {
                int x = 0;
                String line = scanner.nextLine();

                // for every character of the line
                for (char c : line.toCharArray())
                {
                    switch (c)
                    {
                        // wall
                        case '#':
                            maze[x][y] = null;
                            break;

                        // pill
                        case 'o':
                            maze[x][y] = new Node(x, y);
                            maze[x][y].setPillIndex(pillsCounter++);
                            maze[x][y].setNodeIndex(nodeCounter++);
                            nodes.add(maze[x][y]);
                            pillNodes.add(maze[x][y]);
                            break;

                        // power pill
                        case 'X':
                            maze[x][y] = new Node(x, y);
                            maze[x][y].setPowerIndex(powersCounter++);
                            maze[x][y].setNodeIndex(nodeCounter++);
                            nodes.add(maze[x][y]);
                            powerPillNodes.add(maze[x][y]);
                            break;

                        // empty node, but still part of the map
                        case ' ':
                            maze[x][y] = new Node(x, y);
                            maze[x][y].setNodeIndex(nodeCounter++);
                            nodes.add(maze[x][y]);
                            break;

                        case 'P':
                            maze[x][y] = new Node(x, y);
                            maze[x][y].setNodeIndex(nodeCounter++);
                            nodes.add(maze[x][y]);
                            pacmanStart = maze[x][y];
                            break;

                        case 'G':
                            maze[x][y] = new Node(x, y);
                            maze[x][y].setNodeIndex(nodeCounter++);
                            nodes.add(maze[x][y]);
                            ghostStart = maze[x][y];
                            break;
                    }

                    x++;
                }
                y++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Nodes lists

    public Node getNode(int x, int y) {
        return maze[x][y];
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Node> getPillsNodes() {
        return pillNodes;
    }

    public List<Node> getPowerPillsNodes() {
        return powerPillNodes;
    }

    public int getEdibleTimer() {
        return 160;
    }

    public int distance(Node a, Node b) {
        return nodeDistances[a.getNodeIndex()][b.getNodeIndex()];
    }

    public Node getGhostStart() {
        return ghostStart;
    }

    public Node getPacmanStart() {
        return pacmanStart;
    }

    // Methods for map creation

    private void setNeighbors() {
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                if (maze[x][y] != null) {
                    Node currentNode = maze[x][y];
                    // Top
                    if (y > 0 && maze[x][y - 1] != null) {
                        currentNode.addNeighbor(maze[x][y - 1]);
                    }
                    // Down
                    if (y < (rows - 1) && maze[x][y + 1] != null) {
                        currentNode.addNeighbor(maze[x][y + 1]);
                    }
                    // Left
                    if (x > 0 && maze[x - 1][y] != null) {
                        if (x == (cols - 1)) {
                            currentNode.addNeighbor(maze[0][y]);
                        }
                        currentNode.addNeighbor(maze[x - 1][y]);
                    }
                    // Right
                    if (x < (cols - 1) && maze[x + 1][y] != null && maze[x + 1][y] != null) {
                        // Left Tunnels
                        if (x == 0) {
                            currentNode.addNeighbor(maze[cols - 1][y]);
                        }
                        currentNode.addNeighbor(maze[x + 1][y]);
                    }
                }
            }
        }
    }

    private void setDistances()
    {
        nodeDistances = new int[nodes.size()][nodes.size()];

        for (int i = 0; i < nodes.size(); i++) {
            setMax(nodeDistances[i]);
            PacUtil.search(nodes, nodeDistances[i], i, 0);
//            search(nodes, nodeDistances[i], i, 0);
        }
    }

    private void setMax(int[] a) {
        for (int i=0; i<a.length; i++) {
            a[i] = Integer.MAX_VALUE;
        }
    }

    private void search(List<Node> allNodes, int[] distances, int curNodeIndex, int curDist)
    {
        if (curDist < distances[curNodeIndex])
        {
            distances[curNodeIndex] = curDist;
            for (Node n : allNodes.get(curNodeIndex).getNeighbors()) {
                search(allNodes, distances, n.getNodeIndex(), curDist + 1);
            }
        }
    }

    private void saveMap(File file) {
        try
        {
            PrintWriter pw = new PrintWriter(file);

            for (int y = 0; y < rows; y++)
            {
                for (int x = 0; x < cols; x++)
                {
                    // if it is a path node
                    if (maze[x][y] != null)
                    {
                        // if this node is a pill container
                        if (maze[x][y].getPillIndex() >= 0) {
                            pw.write("o");
                        }
                        // if this node is a power pill container
                        else if (maze[x][y].getPowerIndex() >= 0) {
                            pw.write("X");
                        } else {
                            pw.write(" ");
                        }
                    } else {
                        pw.write("#");
                    }
                }

                pw.println();
            }

            pw.flush();
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
