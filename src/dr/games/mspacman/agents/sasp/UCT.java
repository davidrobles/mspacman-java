package dr.games.mspacman.agents.sasp;

/*
 * Spyridon Samothrakis, 07-Feb-2010
 * An implementation of UCT+
 * Algorithm as presented in  
 * http://hal.inria.fr/docs/00/12/15/16/PDF/RR-6062.pdf
 * and 
 * http://www.springerlink.com/content/l7v1647363415h1t/
 */

//import java.text.NumberFormat;

import java.util.ArrayList; //import java.util.Random;
import java.util.Random;

/**
 * @author spyros
 * 
 */
public class UCT {

	public static final boolean DEBUG = false;

	protected Node rootNode;
	private Random generator = new Random();
	// will do a fixed number of simulations if WAITING_TIME_IN_MS is >
	// 999999999
	private static final int SIMULATIONS = 100;
	private static final int TREE_POLICY_MAX_DEPTH = Integer.MAX_VALUE;
	private static final int TREE_POLICY_MIN_DEPTH = 0;
	private static final boolean ENABLE_CACHES = false;
	// private static final int WAITING_TIME_IN_MS = 800;
	private static final int WAITING_TIME_IN_MS = 50;
	private static boolean ENABLE_DEFAULT_POLICY = true;

	private static final boolean PRINT_ACTIONS = false;
	private static boolean isFirstMove = false;

	// 1 is the original UCT
	// 2 is UCT-TUNED ( mostly for GO though...)
	// 3 is e-greedy, try different settings
	private static final int UCT_TYPE = 2;

	// these two are only used by the e-greedy strategy, the need tuning
	private static final double GREEDY_c = 0.99;
	private static final double GREEDY_d = 0.01;

	private long timeStart = 0;
	private long timeEnd = 0;

	public Node getRootNode() {
		return rootNode;
	}

	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}

	public UCT() {

	}

	public UCT(Node rootNode) {
		super();
		this.rootNode = rootNode;
	}

	public double getRootNodeValue() {
		return this.rootNode.value;
	}

	public Action getBestMove(long time) {

		int i = 0;
		int simulations = SIMULATIONS;
		if (isFirstMove) {
			time = time * 2;
			simulations = SIMULATIONS * 2;
			isFirstMove = false;
		}
		timeStart = System.currentTimeMillis();
		timeEnd = time;

		// System.gc();

		boolean shouldBreak = false;

		for (;;) {
			// Do a simulation
			i += 1;
			shouldBreak = playOneSequence(rootNode);
			if (time > 999999999) {
				if (i > simulations) {
					shouldBreak = true;
				}
			}

			// long elapsed = System.currentTimeMillis() - timeStart;
			// System.err.println(shouldBreak);
			if (shouldBreak) {

				break;
			}
		}
		Action bMove = getHighestScoringChild();

		if (PRINT_ACTIONS) {
			System.err.println("Total Time Spent is: "
					+ (double) (System.currentTimeMillis() - timeStart) + "ms");
			System.err.println("Total Simulations: " + i);
		}

		return bMove;

	}

	public Action getBestMove(Node node) {
		if (rootNode == null) {
			rootNode = node;
		}
		return getBestMove(WAITING_TIME_IN_MS, node);
	}

	/**
	 * The core entry method.
	 * 
	 * @param time
	 *            The available simulation time
	 * @return A move
	 */

	public Action getBestMove(long time, Node node) {
		// check the grandchildren of the old rootNode
		if (ENABLE_CACHES) {
			ArrayList<Node> children = rootNode.getChildren();
			ArrayList<Node> grandChildren = new ArrayList<Node>();
			for (int i = 0; i < children.size(); i++) {
				ArrayList<Node> i_grandChildren = children.get(i).getChildren();
				grandChildren.addAll(i_grandChildren);
			}
			for (int i = 0; i < grandChildren.size(); i++) {

				Node cNode = grandChildren.get(i);
				if (cNode.equals(node)) {
					// TronNode n = (TronNode) cNode;
					// n.getTt().drawImagination();
					// ArrayList<Node> grandgrandChildren = cNode.getChildren();
					// for (int j = 0; j < grandgrandChildren.size(); j++) {
					// TronNode n2 = (TronNode) grandgrandChildren.get(j);
					// System.err.println(n2.nb);
					// System.err.println(n2.value);
					// System.err.println(n2.getMove());
					// }
					rootNode = cNode;
					return getBestMove(time);
				}
			}

		}
		rootNode = node;

		// Force GC!!!!

		return getBestMove(time);

	}

	Action getHighestScoringChild() {

		ArrayList<Node> children = rootNode.getChildren();

		// if(children.size() == 0 ){
		// return rootNode.getMove();
		// }

		double max = Integer.MIN_VALUE;
		int index = -1;
		// System.out.println(max);
		Node cNode = null;
		for (int i = 0; i < children.size(); i++) {
			cNode = children.get(i);

			double cVal = cNode.value / (double) cNode.nb;
			// double cVal = ((double) cNode.nb);
			if (PRINT_ACTIONS) {
				System.err.println("Possible Move: " + ", Action "
						+ cNode.action + ", Node.nb " + cNode.nb
						+ ", Node.value " + cNode.value + ", average value "
						+ cNode.value / (double) cNode.nb);

			}

			if (cVal > max) {
				index = i;
				max = cVal;
			}
		}

		if (PRINT_ACTIONS) {
			for (int c = 0; c < children.size(); c++) {
				System.err
						.println("----------------------------------------------");

				ArrayList<Node> grandChildren = children.get(c).getChildren();
				for (int i = 0; i < grandChildren.size(); i++) {
					cNode = (Node) grandChildren.get(i);

					double cVal = (double) cNode.nb;
					System.err.println("GrandChild Possible Move Value " + cVal
							+ ", Action " + cNode.action + ", Node.nb "
							+ cNode.nb + ", Node.value " + cNode.value
							+ ", average value " + cNode.value
							/ (double) cNode.nb);
					System.err
							.println("----------------------------------------------");
					// ArrayList<Node> grandChildren1 =
					// grandChildren.get(index).getChildren();
					// for (int j = 0; j < grandChildren1.size(); j++) {
					// cNode = (Node) grandChildren1.get(j);
					//
					// cVal = (double) cNode.nb;
					// System.err.println("GrandChild Possible Move Value " +
					// cVal
					// + ", Action " + cNode.action + ", Node.nb " + cNode.nb
					// + ", Node.value " + cNode.value);
					//			
					// }
				}
			}

			System.err
					.println("==============================================");
		}
		return children.get(index).action;

	}

	public boolean playOneSequence(Node i_rootNode) {

		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes.add(i_rootNode);

		Node node = i_rootNode;
		// System.err.println("Starging Loop");
		// just for two people games, pick a random move for the opponent
		for (int i = 0;; i++) {
			node = descendByUCB1(nodes.get(i));
			// System.err.println(i + "inside UCT");
			long elapsed = System.currentTimeMillis() - timeStart;
			// if(i > 100)
			// System.out.println(i + "=" + node.toString());
			if (elapsed > timeEnd) {
				// we are running out of time get out FAST
				return true;
			}

			nodes.add(node);
			// These can be put in the for loop, but it's easier to see what's
			// going on here

			if (node.isLeaf()) {
				// System.out.println("hit a leaf");
				break;
			}

			if (!node.visited && node.canBeEvaluated() && ENABLE_DEFAULT_POLICY
					&& i > TREE_POLICY_MIN_DEPTH) {
				node.visited = true;
				break;
			}

			if (i >= TREE_POLICY_MAX_DEPTH && node.canBeEvaluated()) {
				break;
			}
		}
		// System.out.println(node.canBeEvaluated());
		ArrayList<Double> reward = node.evaluate();

		// System.err.println("Reward Received: " + reward.size());
		updateValue(nodes, reward);
		return false;

	}

	// private ArrayList<Double> monteCarlo(Node node) {
	// if (!node.isLeaf()) {
	// return node.calculateDefaultPolicy();
	// }
	// return node.reward;
	// }

	private Node descendByUCB1(Node node) {

		ArrayList<Node> children = node.getChildren();
		int childrenSize = children.size();

		// if (children.size() == 0) {
		// return node;
		// }
		// is it our turn to play ?

		// calculate nb
		// double nb = 0;

		int nb = node.nb;
		// for (int i = 0; i < childrenSize; i++) {
		// nb += children.get(i).nb;
		// }

		double max = Double.NEGATIVE_INFINITY;
		int maxNode = -1;

		// System.err.println(nb + " " + node.nb);
		for (int i = 0; i < childrenSize; i++) {
			// System.err.println(nb + " " + node.nb);
			Node childNode = children.get(i);
			if (childNode.nb == 0) {
				childNode.nv = Double.MAX_VALUE;
				maxNode = i;
				break;

			} else {
				double averageX = (childNode.value) / (double) childNode.nb;

				if (UCT_TYPE == 1) {
					childNode.nv = averageX
							+ Math.sqrt((2.0 * Math.log((double) nb))
									/ (double) childNode.nb);

				} else if (UCT_TYPE == 2) {

					double averageS = (childNode.value) * (childNode.value)
							/ (double) childNode.nb;

					double V = +averageS
							- (averageX * averageX)
							+ Math.sqrt(2.0 * Math.log(nb)
									/ (double) (childNode.nb));

					childNode.nv = averageX
							+ Math
									.sqrt((Math.log((double) nb) / (double) childNode.nb)
											* Math.min(1.0 / 4.0, V));

				} else if (UCT_TYPE == 3) {
					childNode.nv = averageX;
				}

				if (childNode.nv > max) {
					maxNode = i;
					max = childNode.nv;
				}

			}
		}

		// 
		if (UCT_TYPE == 3) {
			double e = Math.min(1, (GREEDY_c * childrenSize)
					/ (GREEDY_d * GREEDY_d * nb));
			if (generator.nextDouble() < e) {
				maxNode = generator.nextInt(childrenSize);
			}
		}

		Node nvMax = children.get(maxNode);
		return nvMax;

	}

	private void updateValue(ArrayList<Node> nodes, ArrayList<Double> reward) {
		int nodeSize = nodes.size();

		// for (double d : reward){
		// System.out.print(d + ", ");
		// }
		// System.out.println("=============");
		// assert reward.size() == 4;
		//
		for (int i = nodeSize - 2; i >= 0; i--) {
			Node node = nodes.get(i);
			// if (reward.get(node.rewardId) == 1.0 && node.rewardId == 4 && i
			// == 0) {
			// System.out.println(node.rewardId + ", "
			// + reward.get(node.rewardId) + node.toString());
			//				
			// System.out.println("=============");
			// }

			// if (reward.get(node.rewardId) == 1.0 && node.rewardId == 3) {
			// System.out.println(node.rewardId + ", "
			// + reward.get(node.rewardId) + node.toString());
			//				
			// System.out.println("=============");
			// }
			node.value += reward.get(node.rewardId);
//			node.stats.addValue(reward.get(node.rewardId));
			node.valueSqrt += (reward.get(node.rewardId) * reward.get(node.rewardId));
			node.nb += 1;
		}

	}

}
