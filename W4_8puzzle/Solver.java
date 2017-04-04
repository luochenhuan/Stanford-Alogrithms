import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
class SearchNode implements Comparable<SearchNode> {
    final Board board;
    final SearchNode prev;
    int steps;
    boolean isTwin;
    
    public SearchNode(Board b, SearchNode prev, int steps, boolean isTwin) {
        board = b;
        this.prev = prev;
        this.steps = steps;
        this.isTwin = isTwin;
    }
    
    public int priority() {
        return board.manhattan() + steps;
    }
    
    @Override 
    public int compareTo(SearchNode o) {
      // Break tie
      if (priority() == o.priority())
        return (board.hamming() + steps) - (o.board.hamming() + steps);
      else
        return priority() - o.priority();
    }
  }
  public class Solver {
    private boolean solved;
    private List<Board> sequences = new ArrayList<>();
    private MinPQ<SearchNode> pq = new MinPQ<>();
    
    public Solver(Board initial) {           // find a solution to the initial board (using the A* algorithm)
      SearchNode nd = new SearchNode(initial, null, 0, false);
      SearchNode twinNd = new SearchNode(initial.twin(), null, 0, true);
      pq.insert(nd);
      pq.insert(twinNd);
      SearchNode curSearchNode;
      Board curBoard;
      while (!pq.isEmpty()) {
        curSearchNode = pq.delMin();
        curBoard = curSearchNode.board;
  //      StdOut.println(curBoard);
  //      StdOut.printf("step: %d   isTwin: %b\n", curSearchNode.steps, curSearchNode.isTwin);

        if (curSearchNode.board.isGoal()) {
          solved = !curSearchNode.isTwin;
          if (solved) {
            while (curSearchNode.prev != null) {
              sequences.add(curSearchNode.board);
              curSearchNode = curSearchNode.prev;
            }
            sequences.add(curSearchNode.board);
            Collections.reverse(sequences);
          }
          break;
        }
        
        for (Board next : curBoard.neighbors()) {
          if (curSearchNode.prev != null && !next.equals(curSearchNode.prev.board)) {
            pq.insert(new SearchNode(next, curSearchNode, curSearchNode.steps+1, curSearchNode.isTwin));
          } else if (curSearchNode.prev == null) {
            pq.insert(new SearchNode(next, curSearchNode, curSearchNode.steps+1, curSearchNode.isTwin));
          }
        }
      }
    }
    
    public boolean isSolvable() {            // is the initial board solvable?
      return solved;
    }
    
    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
      if (!isSolvable())
        return -1;
      else
        return sequences.size()-1;
    }
    
    public Iterable<Board> solution() {      // sequence of boards in a shortest solution; null if unsolvable
        if (isSolvable())
            return sequences;
        return null;
    }
    
    public static void main(String[] args) { // solve a slider puzzle (given below)

      // create initial board from file
      In in = new In(args[0]);
      int n = in.readInt();
      int[][] blocks = new int[n][n];
      for (int i = 0; i < n; i++)
          for (int j = 0; j < n; j++)
              blocks[i][j] = in.readInt();
      Board initial = new Board(blocks);

      // solve the puzzle
      Solver solver = new Solver(initial);

      // print solution to standard output
      if (!solver.isSolvable())
          StdOut.println("No solution possible");
      else {
          StdOut.println("Minimum number of moves = " + solver.moves());
          for (Board board : solver.solution())
              StdOut.println(board);
      }
    }
}
