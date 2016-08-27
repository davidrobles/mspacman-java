package dr.games.mspacman.agents.sasp;

import dr.games.mspacman.model.Direction;

public class PacManAction extends Action {

	Direction action = Direction.NEUTRAL;
	String[] Action  = new String[] {
			"UP",
		    "RIGHT",
		    "DOWN",
		    "LEFT",
		    "CENTER"
	};
	
	@Override
	public String toString() {
		return "PacManAction [action=" + /*Action[action] +*/ "]" + "[" + action + "]";
	}


	public PacManAction(Direction i_action) {
		action = i_action;
	}


	@Override
	public Direction getAction() {
		// TODO Auto-generated method stub
		return action;
	}

}
