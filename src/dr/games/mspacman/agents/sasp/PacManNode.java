package dr.games.mspacman.agents.sasp;

import dr.games.mspacman.model.Direction;
import dr.games.mspacman.model.GameState;
import dr.games.mspacman.model.Ghost;
import dr.games.mspacman.model.GhostType;

import java.util.*;

public class PacManNode extends Node {

	Direction firstPacManMove = Direction.NEUTRAL;

	public static GameState original;

	private HashSet legalPacManAction = new HashSet<Integer>();

	public enum playerType {
		PACMAN, GHOST1, GHOST2, GHOST3, GHOST4
	};

	private double depth = 0;
	// private PacManMC pmc = new PacManMC();
	private playerType currentAgentType;
	PacManNode parent;
	GameState gs;
	private boolean leaf = false;
	private double mcScore = -1.0;
	private ArrayList<Double> reward;
	private int mcScoreDifference;
	PacManMC pmc = new PacManMC();

	public PacManNode(GameState i_gs, PacManAction i_action,
			playerType i_type, PacManNode i_parent, int i_rewardId,
			boolean i_isLeaf, ArrayList<Double> i_reward, double i_mcScore,
			int difference, double oldDepth, Direction i_firstPacManMove) {
		gs = i_gs;
		action = i_action;
		currentAgentType = i_type;
		parent = i_parent;
		rewardId = i_rewardId;
		reward = i_reward;
		leaf = i_isLeaf;
		mcScore = i_mcScore;
		depth = oldDepth + 1;
		firstPacManMove = i_firstPacManMove;
		// System.out.println(depth);
	}

	private double getMaxScore() {
		double score = 0;
		score += (220) * 10;
		score += (50) * 4;
		score += ((200 + 400 + 800 + 1600)) * 4.0;
		return score;
	}

	private double getGhostScore(double distance) {
		if (distance < 500000) {
			return 0.0;
		} else {
			return (1.0 - distance / 200.0) / 10.0;
		}
	}

	public ArrayList<Double> calculateDefaultPolicy() {
		// System.out.println("test");
		ArrayList<Double> i_reward = new ArrayList<Double>();
		// System.out.println("2342423423);
		double score = pmc.mc(gs, this.parent.parent.parent.parent.action.getAction());
		// value = gs.getScore();

		// System.out.println(ghostScore1);
		// i_reward.add(getGhostScore(ghostScore1));
		// i_reward.add(getGhostScore(ghostScore2));
		// i_reward.add(getGhostScore(ghostScore3));
		// i_reward.add(getGhostScore(ghostScore4));
		//
		double s = (double) pmc.totalTicks / PacManMC.GAME_TICKS_PER_GAME;
		if (score == 0.0) {

			//System.out.println(s);
			i_reward.add(s / 5.0);
			i_reward.add(1.0);
			i_reward.add(1.0);
			i_reward.add(1.0);
			i_reward.add(1.0);
		} else {
			//System.out.println(original.getScore() + " " + score);
			double newScore = (score - original.getScore())/2000.0;
			//System.out.println(score - original.getScore() + " " + newScore);
			i_reward.add(newScore + 0.5);
			i_reward.add(0.0);
			i_reward.add(0.0);
			i_reward.add(0.0);
			i_reward.add(0.0);
		}
		return i_reward;
	}

	public ArrayList<Double> calculateDefaultPolicy2() {

		double minDistance = 999999;
		double totalDistance = 0;
		double ghostScore1 = gs.getMazeState().getMaze().distance(gs.getPacman().getCurrentNode(),
				gs.getGhosts().get(0).getCurrentNode());
		if (!gs.getGhosts().get(0).isEdible()) {
			totalDistance += ghostScore1;
		}
		if (ghostScore1 < minDistance) {
			minDistance = ghostScore1;
		}
		double ghostScore2 = gs.getMazeState().getMaze().distance(gs.getPacman().getCurrentNode(),
				gs.getGhosts().get(1).getCurrentNode());
		if (!gs.getGhosts().get(1).isEdible()) {
			totalDistance += ghostScore2;
		}
		if (ghostScore2 < minDistance) {
			minDistance = ghostScore2;
		}
		double ghostScore3 = gs.getMazeState().getMaze().distance(gs.getPacman().getCurrentNode(),
				gs.getGhosts().get(2).getCurrentNode());
		if (!gs.getGhosts().get(2).isEdible()) {
			totalDistance += ghostScore3;
		}
		if (ghostScore3 < minDistance) {
			minDistance = ghostScore3;
		}
		double ghostScore4 = gs.getMazeState().getMaze().distance(gs.getPacman().getCurrentNode(),
				gs.getGhosts().get(3).getCurrentNode());
		if (!gs.getGhosts().get(3).isEdible()) {
			totalDistance += ghostScore4;
		}
		if (ghostScore4 < minDistance) {
			minDistance = ghostScore4;
		}

		// System.out.println(totalDistance);

		double pacmanScore = gs.getMazeState().getMaze().distance(gs.getPacman().getCurrentNode(),
				gs.getPacman().getCurrentNode());
		// System.out.println(minDistance);
		// if(minDistance > 1) {

		// }
		// else {
		// minDistance = 0.5;
		// }
		// System.out.println(minDistance);
		minDistance = minDistance / 1000.0;
		//minDistance = (1.0 - minDistance) / 2.0;

		totalDistance = totalDistance / 2000;
		//totalDistance = (1.0 - totalDistance);

		ArrayList<Double> i_reward = new ArrayList<Double>();
		System.out.println(minDistance);
		double score = ((double) gs.getScore() / getMaxScore());
		i_reward.add(score / 2.0 + minDistance);
		i_reward.add(0.0);
		i_reward.add(0.0);
		i_reward.add(0.0);
		i_reward.add(0.0);

		return i_reward;
	}

	@Override
	public String toString() {
		return "PacManNode [currentAgentType=" + currentAgentType + ", action="
				+ action + ", value=" + value + "]";
	}

	@Override
	public void generateChildren() {
		List<Direction> possibleActions = null;
		playerType nextPlayerType = playerType.PACMAN;
		GameState nextGameState = gs;
		int nextRewardId = 0;

		switch (currentAgentType) {
		case PACMAN: {
			possibleActions = gs.getPacmanMoves();
			if (parent != null) {
				firstPacManMove = this.parent.parent.parent.parent.action
						.getAction();
			}
			// for(int action: possibleActions){
			// legalPacManAction.add(action);
			// }
			nextPlayerType = playerType.GHOST1;
			nextRewardId = 0;
			// System.out.println(nextPlayerType+"==");
			// for(int i : possibleActions){
			// System.out.println("Before " + i);
			// }
			possibleActions.remove(firstPacManMove.opposite());
			// for(int i : possibleActions){
			// System.out.println("After " + i);
			// }
			// System.err.println(previousPacManMove + " " +
			// getOpposite(previousPacManMove));
			break;
		}
		case GHOST1: {
			possibleActions = gs.getGhostMoves(gs.getGhosts().get(0));
			nextPlayerType = playerType.GHOST2;
			nextRewardId = 1;
			// System.out.println(nextPlayerType+"==" + possibleActions.size());
			break;
		}
		case GHOST2: {
			possibleActions = gs.getGhostMoves(gs.getGhosts().get(1));
			nextPlayerType = playerType.GHOST3;
			// System.out.println(nextPlayerType+"==");
			nextRewardId = 2;
			break;
		}
		case GHOST3: {
			possibleActions = gs.getGhostMoves(gs.getGhosts().get(2));
			nextPlayerType = playerType.GHOST4;
			// System.out.println(nextPlayerType+"==");
			nextRewardId = 3;
			break;
		}
		case GHOST4: {
			possibleActions = gs.getGhostMoves(gs.getGhosts().get(3));
			nextPlayerType = playerType.PACMAN;
			// System.out.println(nextPlayerType+"==");
			nextRewardId = 4;
			break;
		}

		}
		// System.out.println(this.toString());
		// System.out.println(currentAgentType);
		// System.out.println(nextPlayerType);
		// System.out.println("=================");
		for (Direction action : possibleActions) {
			boolean nextLeaf = false;
			ArrayList<Double> i_reward = new ArrayList<Double>();
			double nMcScore = 0;
			int i_mscD = 0;
			if (currentAgentType == playerType.GHOST4) {
				// System.out.println(action);
				int oldLives = gs.getLives();
				int oldScore = gs.getScore();
				nextGameState = getNextState(gs, action);
				int newLives = nextGameState.getLives();
				double newScore = nextGameState.getScore() / getMaxScore();

				nMcScore = newScore;
				// i_mscD = newScore - oldScore;

				if ((oldLives != newLives)) {

					i_reward.add(0.0);
					// double ghostReward =
					// 1.0-((double)gs.getScore())/getMaxScore();
					double ghostReward = 1.0;
					i_reward.add(ghostReward);
					i_reward.add(ghostReward);
					i_reward.add(ghostReward);
					i_reward.add(ghostReward);

					nextLeaf = true;
				}

				int end = nextGameState.getMazeState().getPillsBitSet().cardinality()
						+ nextGameState.getMazeState().getPowersBitSet().cardinality();
				// System.out.println(end);
				if (end == 0) {
					System.out.println("END");
					i_reward.add(1.0);

					i_reward.add(0.0);
					i_reward.add(0.0);
					i_reward.add(0.0);
					i_reward.add(0.0);

					nextLeaf = true;

				}
			}

			//
			// for (double d : i_reward){
			// System.out.print(d + ", ");
			// }
			//
			Direction firstMove = firstPacManMove;
			if (parent != null && firstPacManMove == Direction.NEUTRAL
					&& currentAgentType == playerType.PACMAN) {
				firstMove = this.parent.parent.parent.parent.action.getAction();
			}
			// System.out.println(firstMove);
			children.add(new PacManNode(nextGameState, new PacManAction(
					action), nextPlayerType, this, nextRewardId, nextLeaf,
					i_reward, nMcScore, i_mscD, depth, firstMove));
		}

	}

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

	@Override
	public boolean isLeaf() {
		return leaf;
	}

	@Override
	public boolean canBeEvaluated() {
		if (currentAgentType == playerType.PACMAN && this.parent != null) {
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<Double> evaluate() {
		// ArrayList<Double> reward = null;
		if (isLeaf()) {
			return reward;
		} else {
			// System.err.println(initialised + " default");
			return calculateDefaultPolicy();
		}

	}

	private GameState getNextState(GameState gs, Direction action) {

		GameState nextGameState = gs.copy();

		Direction pacManAction = this.parent.parent.parent.action.getAction();
		// previousPacManMove = pacManAction;
		// System.out.println(this.parent.parent.parent.action);
		Direction g1Action = this.parent.parent.action.getAction();
		Direction g2Action = this.parent.action.getAction();
		Direction g3Action = this.action.getAction();
		Direction g4Action = action;

        Map<Ghost, Direction> ghostsDirs = new HashMap<Ghost, Direction>();
        ghostsDirs.put(gs.getGhosts().get(0), g1Action);
        ghostsDirs.put(gs.getGhosts().get(1), g2Action);
        ghostsDirs.put(gs.getGhosts().get(2), g3Action);
        ghostsDirs.put(gs.getGhosts().get(3), g4Action);

		nextGameState.nextState(pacManAction, ghostsDirs);

		// for (GhostState g : nextGameState.getGhosts()) {
		// if(g.edible() == true) {
		// System.out.println("Ghost edible " +
		// nextGameState.getEdibleGhostScore() + " Location " +
		// g.current.toString());
		// }
		// // if (!g.edible() && !g.returning() && overlap(g, pacMan)) return
		// true;
		// }
		// System.out.println("===========");
		return nextGameState;

	}

	@Override
	public boolean equals(Object obj) {
		PacManNode node = (PacManNode) obj;
		if (node.gs.equals(this.gs)
				&& currentAgentType == node.currentAgentType) {
			return true;
		}
		return false;
	}
}
