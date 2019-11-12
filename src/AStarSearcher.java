import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}


	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// FILL THIS METHOD

		// explored list is a Boolean array that indicates if a state associated with a given position in the maze has already been explored. 
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ... edited
		// initialize the root state
		this.initialize(explored);

		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();

		// add to frontier queue
		// use frontier.add(...) to add stateFValue pairs
		frontier.add(new StateFValuePair(new State(maze.getPlayerSquare(), null, 0, 0), 0 + calcHVal(maze.getPlayerSquare(), maze.getGoalSquare())));

		maxSizeOfFrontier = 1;

		
		StateFValuePair expandedStateFValuePair = null;
		

		while (!frontier.isEmpty()) {
			//  maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			noOfNodesExpanded += 1;

			// use frontier.poll() to extract the minimum stateFValuePair.
			
			expandedStateFValuePair = frontier.poll();

			
			State currState = expandedStateFValuePair.getState();
			cost = currState.getGValue();

			explored[currState.getX()][currState.getY()] = true;
			ArrayList<State> childrenStates = currState.getSuccessors(explored, maze);
			// inefficient
			for (State child : childrenStates) {
				calcFVal(child, frontier);
			}

			maxDepthSearched = Math.max(currState.getDepth(), maxDepthSearched);
			maxSizeOfFrontier = Math.max(frontier.size(), maxSizeOfFrontier);

			if (currState.isGoal(maze)) {
				State parentGoal = currState.getParent();
				// update the maze if a solution found
				while (parentGoal != null && parentGoal.getGValue() != 0) {
					maze.setOneSquare(parentGoal.getSquare(), '.');
					parentGoal = parentGoal.getParent();
				}
			//  return true if a solution has been found
				return true;
			}
		}
		// return false if no solution
		return false;	
	}
	/**
	 * Set all positions to false and find the start position
	 * @param explored
	 */
	private void initialize(boolean[][] explored) {
		for (int i = 0; i < explored.length; i++) {
			for (int j = 0; j < explored[i].length; j++) {
				explored[i][j] = false;
			}
		}

		for (int i = 0; i < maze.getNoOfRows(); i++) {
			for (int j = 0; j < maze.getNoOfCols(); j++) {
				if (maze.getSquareValue(i, j) == 'S') {
					return;
				}
			}
		}
	}

	/**
	 * Calculates the current f value
	 * @param successor
	 * @param frontier
	 */
	private void calcFVal(State successor, PriorityQueue<StateFValuePair> frontier) {
		// wasn't able to use arraylist and object casting wouldn't let me change
		Object[] myQueue = frontier.toArray();
		boolean done = false;
		
		for (int i = 0; i < myQueue.length; i++) {
			StateFValuePair iter = (StateFValuePair) myQueue[i];
			if(iter.getState().getX() == successor.getX() && iter.getState().getY() == successor.getY()){
				done = true;
				if (iter.getState().getGValue() < successor.getGValue()) {
					frontier.remove(iter);
					frontier.add(new StateFValuePair(successor,successor.getGValue() + calcHVal(successor.getSquare(), maze.getGoalSquare())));
					break;
				}
			}
		}
		if(!done){
			frontier.add(new StateFValuePair(successor, successor.getGValue() + calcHVal(successor.getSquare(), maze.getGoalSquare())));
		}
	}

	private int calcHVal(Square curr, Square goalSquare) {
		int hueristic = 0;
		hueristic += Math.abs(curr.X - goalSquare.X) + Math.abs(curr.Y - goalSquare.Y);
		return hueristic;
	}

}
