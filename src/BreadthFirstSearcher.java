import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public BreadthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main breadth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		// FILL THIS METHOD
		State currState = null;
		// explored list is a 2D Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...
		this.initialize(explored);

		// Queue implementing the Frontier list
		LinkedList<State> queue = new LinkedList<State>(); 
		queue.add(new State(maze.getPlayerSquare(), null, 0,0));
		maxSizeOfFrontier = Math.max(maxSizeOfFrontier, queue.size());

		while (!queue.isEmpty()) {
			currState = queue.pop();
			// maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			cost = currState.getGValue();
			maxDepthSearched = Math.max(currState.getDepth(), maxDepthSearched);

			explored[currState.getX()][currState.getY()] = true;
			if(currState.isGoal(maze)){
				State parentGoal = currState.getParent();
				// update the maze if a solution found
				while (parentGoal != null && parentGoal.getGValue() != 0) {
					maze.setOneSquare(parentGoal.getSquare(), '.');
					parentGoal = parentGoal.getParent();
				}
				// return true if find a solution
				return true;
			}


			noOfNodesExpanded += 1;




			ArrayList<State> successors = currState.getSuccessors(explored, maze);

			for(State next : successors){
				boolean toAdd = match(next, queue);
				if(toAdd==true){
					// use queue.add(...) to add elements to queue
					queue.add(next);
				}

			}
//			System.out.println(maxSizeOfFrontier + " " + queue.size());
			maxSizeOfFrontier = Math.max(maxSizeOfFrontier, queue.size()+1);
		}

		// TODO return false if no solution
		return false;
	}
	/**
	 * Set all positions to false and find the start position
	 * @param explored
	 */
	private void initialize(boolean[][] explored) {
		for(int i=0; i<explored.length; i++){
			for(int j=0; j<explored[i].length; j++){
				explored[i][j] = false;
			}
		}
		int[] startPos = new int[2]; // ref for debugging
		// System.out.println(startPos[0]);
		for(int i=0; i<maze.getNoOfRows(); i++){
			for(int j=0; j<maze.getNoOfCols(); j++){
				if(maze.getSquareValue(i, j) == 'S'){
					startPos[0] = i;
					startPos[1] = j;
					break;
				}
			}
		}		
	}
	/**
	 * check for successor matches in queue
	 * @param next
	 * @param queue
	 * @return
	 */
	private boolean match(State next, LinkedList<State> queue) {
		for(int i=0; i<queue.size(); i++){
			if(next.getX() == queue.get(i).getX() && next.getY() == queue.get(i).getY()){
				return false;
			}
		}
		return true;
	}
}
