package dr.games.mspacman.agents.sasp;

import dr.games.mspacman.model.Direction;


public abstract class Action {

	enum ActionType { DISCRETE, CONTINUOUS };
	
	public abstract Direction getAction();
	

}
