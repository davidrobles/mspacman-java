package dr.games.mspacman.agents.mc;

import dr.games.mspacman.model.Direction;
import dr.games.mspacman.model.GameState;
import dr.games.mspacman.model.Node;
import dr.games.mspacman.model.PacAgent;
import dr.games.mspacman.util.PacUtil;

import java.util.List;
import java.util.Random;


/**
 * A controller for Pac-Man that uses Monte-Carlo simulations of the
 * next possible states, but always staying on the same direction.
 */
public class NewPureMC implements PacAgent {

    private static final int SIMULATIONS_PER_MOVE = 20;
    private static final int TIME_STEPS_PER_SIMULATION = 20;
    private static final Random rand = new Random();
    
    @Override
    public Direction direction(GameState gameState)
    {
        Direction bestDirection = null;
        double bestScore = Integer.MIN_VALUE;

        List<Direction> pacmanMoves = gameState.getPacmanMoves();

        // every possible move for pacman, including going back on the previous direction
        for (Direction move : pacmanMoves)
        {
            double simulationValue = 0;
            
            for (int i = 0; i < SIMULATIONS_PER_MOVE; i++)
            {
                // make a copy of the game state for the simulation
                GameState playGameState = gameState.copy();

                // temp data
                int startScore = playGameState.getScore();
                int startTimeStep = playGameState.getTimeSteps();
                int maxTimeStepToPlay = startTimeStep + TIME_STEPS_PER_SIMULATION;

                // make the first move with the current simulated move and using neutral moves for the ghosts
                playGameState.nextState(move, PacUtil.ghostNeutralMoves(playGameState.getGhosts()));

                while (!playGameState.isGameOver() && !playGameState.lostLifeOnLastTimeStep()
                        && playGameState.getTimeSteps() < maxTimeStepToPlay)
                {
                    // Pick a random move on the same direction
                    List<Direction> pacMoves = playGameState.getPacmanMoves();
                    pacMoves.remove(playGameState.getPacman().getDirection().opposite());
                    Direction pacmanSimMove = pacMoves.get(rand.nextInt(pacMoves.size()));

                    // make the moves
                    playGameState.nextState(pacmanSimMove,
                            PacUtil.ghostNeutralMoves(playGameState.getGhosts())); // TODO: fix!

                    
                    
                }

                if (playGameState.lostLifeOnLastTimeStep()) {
                    simulationValue += -1000;
                } else {
                    simulationValue += playGameState.getScore() - startScore; // score obtained during the simulation
                }

                if (simulationValue > bestScore) {
                    bestScore = simulationValue;
                    bestDirection = move;
                }

            }

                        // if the simulations for this move don't have any pills, power pills, edible ghosts
            if (simulationValue == 0)
            {
                // get the nearest pill
                Node pacmanCurrentNode = gameState.getPacman().getCurrentNode();
                Node nearestPillNode = PacUtil.nearestPill(pacmanCurrentNode, gameState.getMazeState());

                Direction dir = PacUtil.direction(pacmanCurrentNode, nearestPillNode,
                        gameState.getMazeState().getMaze());

                if (dir == move) {
                    simulationValue = 1;
                }
			}
        }

        return bestDirection;
    }

}
