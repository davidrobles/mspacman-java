package dr.games.mspacman.agents.sasp;

import dr.games.mspacman.model.Direction;
import dr.games.mspacman.model.GameState;
import dr.games.mspacman.util.PacUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PacManMC {

	Random rand = new Random();
	// private Tree<Node> tree;
	// private List<Path> paths = new ArrayList<Path>();

	public static GameState original;
	public int totalTicks;


	public static final int GAME_TICKS_PER_GAME = 50;
	private static final double SIMULATION_NUMBER = 1;

	private int getOpposite(int direction) {
		if (direction == 0)
			return 2;
		if (direction == 2)
			return 0;
		if (direction == 3)
			return 1;
		if (direction == 1)
			return 3;
		// System.out.println("WHAT THE FUCK" + direction);
		return 4;
	}
	
	public double mc(GameState gs, Direction previousAction) {
		double sum = 0 ;
		totalTicks = 0;
		for(int i = 0; i < SIMULATION_NUMBER; i ++) {
			sum+=mcHelper(gs,previousAction);
		}
		//System.out.println(totalTicks);
		return sum/SIMULATION_NUMBER;
		
	}

	public double mcHelper(GameState gs, Direction previousAction) {

		List<Direction> possibleMoves = gs.getPacmanMoves();

		possibleMoves.remove(previousAction.opposite());
		//int curScore = gs.getScore();
		GameState playGS = gs.copy();
		boolean liveLost = false;
		int maxGameTick = playGS.getTimeSteps() + GAME_TICKS_PER_GAME;
		Direction previousMove = previousAction;
		
		int livesBefore = playGS.getLives();
		while (!liveLost && playGS.getTimeSteps() < maxGameTick) {
			List<Direction> gameMoves = playGS.getPacmanMoves();

			gameMoves.remove(previousMove.opposite());
		

			int randomNumber = rand.nextInt(gameMoves.size());
			previousMove = gameMoves.get(randomNumber);
			//System.out.println(previousMove);
			playGS.nextState(previousMove, PacUtil.ghostNeutralMoves(gs.getGhosts()));
			int  end = playGS.getMazeState().getPillsBitSet().cardinality()
			+ playGS.getMazeState().getPowersBitSet().cardinality();
			if(end == 0 ) {
				return getMaxScore();
			}
			int livesAfter = playGS.getLives();
			// if lost live
			if (livesBefore != livesAfter) {
				// System.out.println("lost live");
				liveLost = true;
				break;
				
			}
			//
			totalTicks+=1;
		}
		
		double playScore = evaluatePlay(playGS, liveLost, previousAction);
		return playScore;

	}

	double evaluatePlay(GameState gs, boolean liveLost, Direction action) {

		double eval = 0;
		if (liveLost) {
			return 0.0;
		}
		return gs.getScore();

	}

	private double getMaxScore() {
		double score = 0;
		score+=(220)*10;
		score+=(50)*4;
		score+=((200+400+800+1600))*4.0;
		return score;
	}

}