package dr.games.mspacman.agents.sasp;


/*
 * Spyridon Samothrakis, 07-Feb-2010
 * An implementation of UCT
 * Algorithm as presented in  
 * http://hal.inria.fr/docs/00/12/15/16/PDF/RR-6062.pdf
 */


import java.util.ArrayList;



public abstract class Node {

	protected enum NodeType { CHANCE, MIN, MAX, EVALUATION };
	protected NodeType type;

	public int nb = 0;
	public double value = 0;
	
	protected double nv = 0;
	protected double valueSqrt = 0;
	protected boolean visited = false;

	protected ArrayList<Node> children = new ArrayList<Node>();
	
//	protected SummaryStatistics  stats = new SummaryStatistics ();
	// the rewards for this node ( if any ) 

	protected int rewardId;
	
	// the action to this node
	public Action action = null;

	public ArrayList<Node> getChildren() {
		if (children.size() == 0 && !isLeaf()) {
			generateChildren();
		}
		return children;
	}


	public abstract boolean isLeaf();
	public abstract void generateChildren();
	public abstract boolean canBeEvaluated();
	public abstract ArrayList<Double>  evaluate();

}
