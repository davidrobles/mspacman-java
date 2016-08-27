package dr.games.mspacman.agents.sasp;

import java.util.ArrayList;

import dr.games.mspacman.model.Direction;
import dr.games.mspacman.model.GameState;
import dr.games.mspacman.model.PacAgent;

public class UCTPacMan implements PacAgent {

	static UCT uct = new UCT();
	static int ticks = 0;
	static int oldAction = 0;
	static int beforeResetScore = 0;
	Node next = null;
	int oldScore = 0;
	int previousAction = 4;
//	NearestPillDistance npd = new NearestPillDistance();
	private int bestAction;

    @Override
    public Direction direction(GameState gs) {

		if (gs.getMazeState().getPillsBitSet().cardinality() == 220) {
			beforeResetScore = gs.getScore();
		}
		gs = gs.copy();
//		((GameState) gs).score = ((GameState) gs).score - beforeResetScore;
//
		PacManNode.original = gs;

		PacManNode pn = new PacManNode(gs, new PacManAction(Direction.NEUTRAL),
				PacManNode.playerType.PACMAN, null, 4, false, null, 0, 1,0,Direction.NEUTRAL);
//		//PacManNode.previousPacManMove = 4;
//
//
//
//		GameStateInterface i_orig = gs.copy();
//		Node current = i_orig.getPacman().current;
//		//System.out.println(gs.getEdibleGhostScore());
//
//		npd.score(i_orig, current);
//
//		next = Utilities.getClosest(current.adj, npd.closest, i_orig
//				.getMaze());
//		//if (oldScore != gs.getScore() || next == null) {
//
//			ticks = 0;
//			oldScore = gs.getScore();
//			System.out.println("=============================");
//			bestAction = Utilities.getWrappedDirection(current, next, gs
//					.getMaze());
//		//}
//
//		//System.out.println(current);
//		//System.out.println(npd.closest);
//
//
//
//
//		ticks += 1;

		Direction result = uct.getBestMove(pn).getAction();

//		ArrayList<sasp777.uct.Node> children = pn.getChildren();
//		Confidence cf = new Confidence(children);
//		if(!cf.areResultsSignificantSasp(1.3)) {
//			return bestAction;
//		}
//		previousAction = result;
		return result;
    }
}