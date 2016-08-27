package dr.games.mspacman.agents.treebased;//package net.davidrobles.games.pacsim.agents.treebased;
//
//import net.davidrobles.games.pacsim.model.ModelInterface;
//import net.davidrobles.games.pacsim.model.PillType;
//import net.davidrobles.games.pacsim.model.AStarNode;
//import net.davidrobles.games.pacsim.maze.Direction;
//import net.davidrobles.games.pacsim.characters.ghosts.Ghost;
//import net.davidrobles.games.pacsim.characters.GenericCharacter;
//import net.davidrobles.games.pacsim.agents.PacAgent;
//import net.davidrobles.games.pacsim.PacUtil;
//import net.davidrobles.util.tree.TreeNode;
//
//import java.util.List;
//import java.util.ArrayList;
//import java.util.Collections;
//
//public class TreeBasedController implements PacAgent {
//
//    private TreeP tree;
//
//    private ModelInterface gameModel;
//
//    private List<? extends Ghost> ghosts;
//
//    private GenericCharacter pacMan;
//
//    private List<TreeBasedControllerObserver> observers = new ArrayList<TreeBasedControllerObserver>();
//
//    private final static int MAX_TREE_HEIGHT = 43;
//
//    // If at any given moment the tree doesn't contains any pill or power pill,
//    // it will select a pill and that node will be the target. It will keep the
//    // same target node if it goest into the same situation.
//    private AStarNode pillNode = null;
//
//    @Override
//    public Direction direction(ModelInterface model) {
//
//        if (model.getPacMan().getCurrentNode() == null) {
//            return Direction.CENTER;
//        }
//
//        this.gameModel = model;
//        ghosts = model.getGhosts();
//        pacMan = model.getPacMan();
//        TreeNodeP root = new TreeNodeP(model.getPacMan().getCurrentNode(), TreeNodePType.PACMAN);
//        makePathsTree(root);
//
//        // stay close to the power pill until a ghost approaches!
//        if (!containsEdibleGhosts() && nearestPowerPillDistance() < 15
//                && (nearestPowerPillToNearestGhostDistance() - 12 > nearestPowerPillDistance())) {
//            notifyObservers();
//            return Direction.CENTER;
//        } else if (nearestPowerPillDistance() < 15
//                && (nearestPowerPillToNearestGhostDistance() - 12 <= nearestPowerPillDistance())) {
//            notifyObservers();
//            return PacUtil.shortestPathMoveDirection(gameModel.getMaze(), pacMan.getCurrentNode(), nearestPowerPill());
//        }
//
//        // this should be part of the view?
//        for (Path path : nonSafePaths()) {
//            for (TreeNodeP tn : path.getTreeNodes()) {
//                tn.setHighlight(true);
//            }
//        }
//
//        // this should be part of the view?
//        for (Path path : safePaths()) {
//            for (TreeNodeP tn : path.getTreeNodes()) {
//                tn.setHighlight(false);
//            }
//        }
//
//        // if there is at least one safe path
//        if (safePaths().size() > 0) {
//
//            // if pacman is being followed close by a ghost, it should go to a path
//            // with less pills to move faster
//            if (nearestGhostDistance() < 12) {
//                notifyObservers();
//                return directionToPath(pathWithLessPills(safePaths()));
//            }
//
////            List<Path> bestPaths = safePaths();
////            Collections.sort(bestPaths);
////
////            for (Path path : bestPaths) {
////                if (pacman.getPrevNode() != null) {
////                    if (gameModel.getPacman().oppositeDirection() != directionToPath(path)) {
////                        updateObservers();
////                        return directionToPath(path);
////                    }
////                }
////            }
//
//            // if there are edible ghosts on any part of the maze
//            if (containsEdibleGhosts()) {
//                // if there is an edible ghost in a safe path
//                if (pathsWithEdibleGhosts(safePaths()).size() > 0) {
//                    Path pathToGo = null;
//                    int distance = Integer.MAX_VALUE;
//                    for (Path path : pathsWithEdibleGhosts(safePaths())) {
//                        for (TreeNodeP treeNode : path.getTreeNodes()) {
//                            int tempDist = gameModel.getMaze().distance(treeNode.getElement(),
//                                    path.firstTreeNode().getElement());
//                            if (treeNode.getType() == TreeNodePType.EDIBLE_GHOST && tempDist < distance) {
//                                distance = tempDist;
//                                pathToGo = path;
//                            }
//                        }
//                    }
//                    notifyObservers();
//                    return directionToPath(pathToGo);
//                }
//                // ghosts not in the safe paths
//                else {
//                    Path pathToGo = null;
//                    int bestDistance = Integer.MAX_VALUE;
//                    for (Path path : safePaths()) {
//                        int tempDist = gameModel.getMaze().distance(path.lastTreeNode().getElement(),
//                                nearestEdGhosT(path.lastTreeNode().getElement()).getCurrentNode());
//                        if (tempDist < bestDistance) {
//                            bestDistance = tempDist;
//                            pathToGo = path;
//                        }
//                    }
//                    notifyObservers();
//                    return directionToPath(pathToGo);
//                }
//            }
//
//            // if any of the safe paths contains at least a pill
//            if (!pathsContainPills(safePaths())) {
//                // if there are no pill in the tree, it will look for a pill
//                // somewhere else and set it as a target
//                for (AStarNode node : model.getMaze().getNodes()) {
//                    if ((node.getPillType() == PillType.PILL && pillNode == null)
//                            || (node.getPillType() == PillType.PILL
//                            && pillNode.getPillType() == PillType.EMPTY)) {
//                        pillNode = node;
//                        break;
//                    }
//                }
//                if (pillNode != null) {
//                    Path pathToGo = null;
//                    int distToNextNode = Integer.MAX_VALUE;
//                    for (Path path : safePaths()) {
//                        TreeNode<AStarNode> lastTreeNode = path.lastTreeNode();
//                        int dist = model.getMaze().distance(pillNode, lastTreeNode.getElement());
//                        if (dist < distToNextNode) {
//                            distToNextNode = dist;
//                            pathToGo = path;
//                        }
//                    }
//                    notifyObservers();
//                    return directionToPath(pathToGo);
//                }
//                notifyObservers();
//                return directionToPath(bestPath(safePaths()));
//            }
//
//            notifyObservers();
//            return directionToPath(bestPath(safePaths()));
//        }
//        // if there are no safe paths pacman goes to the path
//        // where the ghost is further
//        else {
//            Path pathToGo = null;
//            int distance = Integer.MIN_VALUE;
//            for (Path path : nonSafePaths()) {
//                for (TreeNodeP treeNode : path.getTreeNodes()) {
//                    if (treeNode.getType() == TreeNodePType.GHOST) {
//                        int tempDist = gameModel.getMaze().distance(treeNode.getElement(),
//                                path.firstTreeNode().getElement());
//                        if (tempDist > distance) {
//                            distance = tempDist;
//                            pathToGo = path;
//                        }
//                    }
//                }
//            }
//            notifyObservers();
//            return directionToPath(pathToGo);
//        }
//    }
//
//    /*
//     * Returns the direction that must be taken in order to go to a path.
//     * This direction is calculated based on the first node of the path.
//     */
//    public Direction directionToPath(Path path) {
//        AStarNode goNode = path.firstTreeNode().getElement();
//        return PacUtil.direction(pacMan.getCurrentNode(), goNode);
//    }
//
//    /*
//     * Creates a tree of the possible paths that
//     * Pac-Man can take. This method uses recursion
//     * to generate the tree, and is limited by the variable
//     * TREE_HEIGHT, so it can't grow infinitely.
//     */
//    private void makePathsTree(TreeNodeP root) {
//        tree = new TreeP(root);
//        makePathsTree(root, null);
//        tree.makePaths();
//    }
//
//    /*
//     * Creates a tree of the possible paths that
//     * Pac-Man can take. This method uses recursion
//     * to generate the tree, and is limited by the variable
//     * TREE_HEIGHT, so it can't grow infinitely.
//     */
//    public void makePathsTree(TreeNodeP treeNode, AStarNode previous) {
//        if (treeNode.depth() < MAX_TREE_HEIGHT) {
//            List<AStarNode> neighbors = treeNode.getElement().getNeighbors();
//            for (AStarNode neighbor : neighbors) {
//                if (!neighbor.equals(previous) && !tree.contains(new TreeNode<AStarNode>(neighbor))) {
//                    TreeNodeP child = new TreeNodeP(neighbor, nodeType(neighbor));
//                    treeNode.addChild(child);
//                    makePathsTree(child, treeNode.getElement());
//                }
//            }
//        }
//    }
//
//    // Returns the type of the content on a AStarNode.
//    // Ex. Power Pill, Pill, Ghost, Empty, etc.
//    // NOT AN ELEGANT WAY, BUT WORKS. SHOULD BE FIXED LATER.
//    public TreeNodePType nodeType(AStarNode node) {
//        for (Ghost ghost : ghosts) {
//            if (ghost.getCurrentNode() != null) {
//                if (ghost.getCurrentNode().equals(node)) {
//                    if (ghost.isEdible()) {
//                        return TreeNodePType.EDIBLE_GHOST;
//                    } else {
//                        return TreeNodePType.GHOST;
//                    }
//                }
//            }
//        }
//        if (node.getPillType() == PillType.POWER_PILL) {
//            return TreeNodePType.POWER_PILL;
//        } else if (node.getPillType() == PillType.EMPTY) {
//            return TreeNodePType.EMPTY;
//        } else if (node.getPillType() == PillType.PILL) {
//            return TreeNodePType.PILL;
//        } else if (node.equals(pacMan.getCurrentNode())) {
//            return TreeNodePType.PACMAN;
//        }
//        return TreeNodePType.EMPTY;
//    }
//
//    // Returns the non safe paths of the tree
//    public List<Path> nonSafePaths() {
//        List<Path> nonSafeList = new ArrayList<Path>();
//        for (Path path : tree.getPaths()) {
//            if (!TreeP.isPathSafe(path)) {
//                nonSafeList.add(path);
//            }
//        }
//        return nonSafeList;
//    }
//
//    /**
//     * Registers an observer of this model. This observer
//     * will receive notifications of updates on every time step of
//     * the game using with the increaseMoveTotal() method.
//     * @param observer observer to be notified of updates on this model
//     */
//    public void registerObserver(TreeBasedControllerObserver observer) {
//        observers.add(observer);
//    }
//
//    public Path pathWithLessPills(List<Path> paths) {
//        Path bestPath = null;
//        int lessPills = Integer.MAX_VALUE;
//        for (Path path : paths) {
//            if (bestPath == null || path.getPillNodes() < lessPills) {
//                bestPath = path;
//                lessPills = path.getPillNodes();
//            }
//        }
//        return bestPath;
//    }
//
//    public Path pathWithMorePills(List<Path> paths) {
//        Path bestPath = null;
//        int bestPills = 0;
//        for (Path path : paths) {
//            if (bestPath == null || path.getPillNodes() > bestPills) {
//                bestPath = path;
//                bestPills = path.getPillNodes();
//            }
//        }
//        return bestPath;
//    }
//
//    public Path pathWithMorePowerPills(List<Path> paths) {
//        Path bestPath = null;
//        int bestPills = 0;
//        for (Path path : paths) {
//            if (bestPath == null || path.getPowerPillNodes() > bestPills) {
//                bestPath = path;
//                bestPills = path.getPowerPillNodes();
//            }
//        }
//        return bestPath;
//    }
//
//
//    public Path pathContainsPowerPills(List<Path> paths) {
//        for (Path path : paths) {
//            if (path.getPowerPillNodes() > 0) {
//                return path;
//            }
//        }
//        return null;
//    }
//
//    /* Returns a list of the safe paths of the tree */
//    public List<Path> safePaths() {
//        List<Path> safeList = new ArrayList<Path>();
//        for (Path path : tree.getPaths()) {
//            if (TreeP.isPathSafe(path)) {
//                safeList.add(path);
//            }
//        }
//        return safeList;
//    }
//
//    public boolean treeContainsEdibleGhosts(List<Path> paths) {
//        for (Path path : paths) {
//            if (path.getEdibleGhostNodes() > 0) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public List<Path> pathsWithEdibleGhosts(List<Path> paths) {
//        List<Path> pathsWithEdibleGhosts = new ArrayList<Path>();
//        for (Path path : paths) {
//            if (path.getEdibleGhostNodes() > 0) {
//                pathsWithEdibleGhosts.add(path);
//            }
//        }
//        return pathsWithEdibleGhosts;
//    }
//
//    public Path pathContainsEdibleGhosts(List<Path> paths) {
//        for (Path path : paths) {
//            if (path.getEdibleGhostNodes() > 0) {
//                return path;
//            }
//        }
//        return null;
//    }
//
//    /* Returns true if there is at least one pill in any of the paths */
//    public boolean pathsContainPills(List<Path> paths) {
//        for (Path path : paths) {
//            if (path.getPillNodes() > 0) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean containsPathWithEdibleGhosts(List<Path> paths) {
//        for (Path path : paths) {
//            if (path.getEdibleGhostNodes() > 0) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public AStarNode findAnyPill() {
//        for (AStarNode node : gameModel.getMaze().getNodes()) {
//            if (node.getPillType() == PillType.PILL) {
//                return node;
//            }
//        }
//        return null;
//    }
//
//    public Path bestPathToNode(List<Path> paths, AStarNode node) {
//        Path bestPath = null;
//        int distance = Integer.MAX_VALUE;
//        for (Path path : paths) {
//            if (bestPath == null ||
//                    gameModel.getMaze().distance(bestPath.lastTreeNode().getElement(), node)< distance) {
//                bestPath = path;
//                distance = gameModel.getMaze().distance(path.lastTreeNode().getElement(), node);
//            }
//        }
//        return bestPath;
//    }
//
//    /* Returns the best path from the given list of paths */
//    private Path bestPath(List<Path> paths) {
//        if (paths.size() == 1) {
//            return paths.get(0);
//        }
//        for (Path path : paths) {
//            path.setValue(valFun(path));
//        }
//        Collections.sort(paths);
//        for (Path path : paths) {
//            if (directionToPath(path) != Direction.oppositeDirection(pacMan.getDirection())) {
//                return path;
//            }
//        }
//        return paths.get(0);
//    }
//
//    public boolean flag = true;
//
//    private double valFun(Path path) {
////        int pacX = gameModel.getPacMan().getCurrentNode().getX();
////        int pacY = gameModel.getPacMan().getCurrentNode().getY();
////        int nextX = path.firstTreeNode().getElement().getX();
////        int nextY = path.firstTreeNode().getElement().getY();
////        int lastX = path.lastTreeNode().getElement().getX();
////        int lastY = path.lastTreeNode().getElement().getY();
//        int nPills = path.getPillNodes();
//        int nPowerPills = path.getPowerPillNodes();
//        int pills = nPills * 10;
//        int powerPills;
//
//        if (flag) {
//            if (totalPills() < 50) {
//                powerPills = nPowerPills * 100;
//            } else {
//                powerPills = 0;
//            }
//        } else {
//            powerPills = nPowerPills * 300;
//        }
//
//
////        double xx = (Math.abs((gameModel.getMaze().getColumns() / 2) - pacX) * 0.4);
////        double yy = (Math.abs((gameModel.getMaze().getRows() / 2) - pacY) * 0.4);
//
//        return pills + powerPills /*- (yy * (gameModel.getMaze().getTotalPills() / 224))*/;
//    }
//
//    private int totalPills() {
//        int i = 0;
//        for (AStarNode node : gameModel.getMaze().getPillsNodes()) {
//            if (node.getPillType() == PillType.PILL) {
//                i++;
//            }
//        }
//        return i;
//    }
//
//    /* Notifies the observers of this model of any updates. */
//    public void notifyObservers() {
//        for (TreeBasedControllerObserver o : observers) {
//            o.update(tree);
//        }
//    }
//
//    // CALCULATIONS
//
//    public Ghost nearestEdGhosT(AStarNode node) {
//        Ghost nearestGhost = null;
//        for (Ghost ghost : ghosts) {
//            if (ghost.isEdible() && (nearestGhost == null
//                    || gameModel.getMaze().distance(node,
//                        ghost.getCurrentNode())
//                    < gameModel.getMaze().distance(node,
//                        nearestGhost.getCurrentNode()))) {
//                nearestGhost = ghost;
//            }
//        }
//        return nearestGhost;
//    }
//
//    public Ghost nearestGhost() {
//        Ghost nearestGhost = null;
//        for (Ghost ghost : ghosts) {
//            if (!ghost.isEdible() && (nearestGhost == null
//                    || gameModel.getMaze().distance(pacMan.getCurrentNode(),
//                        ghost.getCurrentNode())
//                    < gameModel.getMaze().distance(pacMan.getCurrentNode(),
//                        nearestGhost.getCurrentNode()))) {
//                nearestGhost = ghost;
//            }
//        }
//        return nearestGhost;
//    }
//
//    public int nearestGhostDistance() {
//        Ghost nearestGhost = nearestGhost();
//        if (nearestGhost == null || pacMan == null) {
//            return Integer.MAX_VALUE;
//        }
//        return gameModel.getMaze().distance(pacMan.getCurrentNode(), nearestGhost.getCurrentNode());
//    }
//
//    public AStarNode nearestPowerPill() {
//        AStarNode nearestPowerPill = null;
//        for (AStarNode node : gameModel.getMaze().getPowerPillsNodes()) {
//            if (node.getPillType() == PillType.POWER_PILL
//                    && (nearestPowerPill == null
//                    || gameModel.getMaze().distance(pacMan.getCurrentNode(), node)
//                    < gameModel.getMaze().distance(pacMan.getCurrentNode(), nearestPowerPill))) {
//                nearestPowerPill = node;
//            }
//        }
//        return nearestPowerPill;
//    }
//
//    public int nearestPowerPillDistance() {
//        AStarNode nearestPowerPill = nearestPowerPill();
//        if (nearestPowerPill == null || pacMan == null) {
//            return Integer.MAX_VALUE;
//        }
//        return gameModel.getMaze().distance(pacMan.getCurrentNode(), nearestPowerPill);
//    }
//
//    public int nearestPowerPillToNearestGhostDistance() {
//        AStarNode nearestPowerPill = nearestPowerPill();
//        Ghost nearestGhost = nearestGhost();
//        if (nearestPowerPill == null || nearestGhost == null) {
//            return Integer.MAX_VALUE;
//        }
//        return gameModel.getMaze().distance(nearestPowerPill, nearestGhost.getCurrentNode());
//    }
//
//    /* Returns true if there is at least one edible ghost */
//    private boolean containsEdibleGhosts() {
//        for (Ghost ghost : ghosts) {
//            if (ghost.isEdible()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public int totalGhostDistances() {
//        if (!containsEdibleGhosts()) {
//            int count = 0;
//            for (int i = 0; i < ghosts.size() - 2; i++) {
//                Ghost ghost = ghosts.get(i);
//                if (!ghost.isEdible()) {
//                    count += gameModel.getMaze().distance(pacMan.getCurrentNode(),
//                            ghost.getCurrentNode());
//                }
//            }
//            return count;
//        }
//        return Integer.MAX_VALUE;
//    }
//
//}