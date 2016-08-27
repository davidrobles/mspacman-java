package dr.games.mspacman.agents.mc;

import dr.games.mspacman.model.Direction;
import dr.games.mspacman.model.GameState;
import dr.games.mspacman.model.Node;
import dr.games.mspacman.model.PacAgent;
import dr.games.mspacman.util.PacUtil;
import dr.games.mspacman.view.AgentView;

import java.awt.*;
import java.util.List;

/**
 * Monte-Carlo Agent controller for Ms. Pac-Man.
 */
public class PureMCWithDiscount implements PacAgent, AgentView {

    private static final int PLAYS_PER_MOVE = 30;
    private static final int GAME_TICKS_PER_GAME = 60;
    private static final int TIME_PER_MOVE = 10; // in milliseconds

    @Override
    public Direction direction(GameState gameState)
    {
        Direction bestDir = null;
        double bestDirScore = Double.MIN_VALUE;

        long timePerMove = TIME_PER_MOVE / (long) gameState.getPacmanMoves().size();

        for (Direction moveDir : gameState.getPacmanMoves())
        {
            double sum = 0;

            // Play simulations on this direction, never going back to the previous node
            long elapsedStart = System.currentTimeMillis();

            while ((System.currentTimeMillis() - elapsedStart) < timePerMove)
            {
                GameState playGS = gameState.copy();
                double simValue = 0;
                int origLives = playGS.getLives();
                int origScore = playGS.getScore();
                double maxTimeStep = playGS.getTimeSteps() + GAME_TICKS_PER_GAME;
                playGS.nextState(moveDir, PacUtil.ghostNeutralMoves(gameState.getGhosts()));

                int moveScore = playGS.getScore() - origScore;
                double discountRate = (1 - ((maxTimeStep - playGS.getTimeSteps()) / maxTimeStep));
                simValue += moveScore * discountRate;

                // Play until lossing a life or
                while (!playGS.isGameOver() && origLives >= playGS.getLives() && playGS.getTimeSteps() < maxTimeStep)
                {
                    List<Direction> possibleDirs = playGS.getPacmanMoves(); // TODO: copy?
                    // remove the move that takes you to the previous node
                    possibleDirs.remove(playGS.getPacman().getDirection().opposite());
                    // get a random legal move
                    Direction pacDir = possibleDirs.get(PacUtil.rand.nextInt(possibleDirs.size()));

                    // compute data BEFORE move
                    int prevScore = playGS.getScore();

                    // move to next state
                    playGS.nextState(pacDir, PacUtil.ghostNeutralMoves(playGS.getGhosts()));

                    // compute data AFTER the move
                    moveScore = playGS.getScore() - prevScore;
                    discountRate = (1 - ((maxTimeStep - playGS.getTimeSteps()) / maxTimeStep));
                    simValue += moveScore * discountRate;

                    if (playGS.getLives() < origLives) {
                        simValue -= 1000 * discountRate;
                        break;
                    }
                }

//                double playScore;

//                if (playGS.getLives() < origLives) {
//                    playScore = -1000;
//                } else {
//                    playScore = simValue;
//                }

                sum += simValue;
            }

            // if the simulations for this move don't have any pills, power pills, edible ghosts
//            if (sum == 0)
//            {
//                // get the nearest pill
//                Node pacmanCurrentNode = gameState.getPacman().getCurrentNode();
//                Node nearestPillNode = PacUtil.nearestPill(pacmanCurrentNode, gameState.getMazeState());
//
//                Direction dir = PacUtil.direction(pacmanCurrentNode,
// nearestPillNode, gameState.getMazeState().getMaze());
//
//                if (dir == moveDir) {
//                    sum = 1;
//                }
//			}

            // update the best path
            if (bestDir == null || sum > bestDirScore) {
                bestDir = moveDir;
                bestDirScore = sum;
            }
        }

        return bestDir;
    }

    int evaluatePlay(GameState gs, boolean liveLost, int startScore) {
        return !liveLost ? gs.getScore() - startScore : -1000;
    }

    @Override
    public void drawUnderPills(Graphics2D dbg2D, int startX, int startY, int nodeSize) {

    }

    @Override
    public void drawAbovePills(Graphics2D dbg2D) {

    }

    @Override
    public void drawAboveEverything(Graphics2D dbg2D) {

    }

}