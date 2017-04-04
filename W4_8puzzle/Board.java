import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int[][] board;
    private final int[][] goalBoard;
    private final int n;
    
    public Board(int[][] blocks) {        // construct a board from an n-by-n array of blocks
      n = blocks.length;
      board = new int[n][n];
      for (int i = 0; i < n; i++) {
        board[i] = Arrays.copyOf(blocks[i], n);
      }
      goalBoard = new int[n][n];
      for (int i = 0; i < n*n - 1; i++) {
        goalBoard[i/n][i%n] = i+1;
      }
      goalBoard[n-1][n-1] = 0;
    }
    
    public int dimension() {
      return n;
    }                 
    
    public int hamming() {                  // number of blocks out of place
      int count = 0;
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          if (board[i][j] != 0 && board[i][j] != goalBoard[i][j])
            count++;
        }
      }
      return count;
    }
    
    public int manhattan() {                 // sum of Manhattan distances between blocks and goal
      int dist = 0;
      int r, c;
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          if (board[i][j] != 0) {
            r = (board[i][j]-1)/n;
            c = (board[i][j]-1)%n;
            dist = dist + Math.abs(i-r) + Math.abs(j-c); 
          }
        }
      }
      return dist;
    }
    
    public boolean isGoal() {                // is this board the goal board?
      return hamming() == 0;
    }
    
    public Board twin() {                   // a board that is obtained by exchanging any pair of blocks
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n-1; j++) {
          if (board[i][j] != 0 && board[i][j+1] != 0) {
            return new Board(swap(board, i, j+1, i, j));
          }
         }
      }
      throw new IllegalArgumentException();
    }
    
    @Override
    public boolean equals(Object y) {       // does this board equal y?
      if (!(y instanceof Board))
        return false;
      Board b = (Board) y;
      if (b.dimension() != dimension())
        return false;
      for (int i = 0; i < n; i++) {
          for (int j = 0; j < n; j++) {
              if (board[i][j] != b.board[i][j])
                  return false;
          }
      }
      return true;
    }
    
    public Iterable<Board> neighbors() {    // all neighboring boards
      int [] dx = {0,1,0,-1};
      int [] dy = {1,0,-1,0};
      List<Board> ls = new ArrayList<>();
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          if (board[i][j] == 0) {
            for (int k = 0; k < 4; k++) {
              if (isValid(i+dx[k], j+dy[k])) {
                ls.add(new Board(swap(board, i, j, i+dx[k], j+dy[k])));
              }
            }
          }
        } 
      }
      return ls;
    }
    
    @Override
    public String toString() {               // string representation of this board (in the output format specified below)
      StringBuilder s = new StringBuilder();
      s.append(n + "\n");
      for (int i = 0; i < n; i++) {
          for (int j = 0; j < n; j++) {
              s.append(String.format("%2d ", board[i][j]));
          }
          s.append("\n");
      }
      return s.toString();
    }
    
    private boolean isValid(int r, int c) {
      return r>=0 && r<n && c>=0 && c<n;
    }
    
    private int[][] swap(int[][] array, int r1, int c1, int r2, int c2) {
      int[][] twinBoard = new int[n][n];
      for (int i = 0; i < n; i++) {
        twinBoard[i] = Arrays.copyOf(array[i], n);
      }
      int temp = twinBoard[r1][c1];
      twinBoard[r1][c1] = twinBoard[r2][c2];
      twinBoard[r2][c2] = temp;
      return twinBoard;
    }
    
    private int[][] getGoalBoard() {
      return goalBoard;
    }
    public static void main(String[] args) {
      // create initial board from file
      In in = new In(args[0]);
      int n = in.readInt();
      int[][] blocks = new int[n][n];
      for (int i = 0; i < n; i++)
          for (int j = 0; j < n; j++)
              blocks[i][j] = in.readInt();
      Board initial = new Board(blocks);
      StdOut.println("initial board");
      StdOut.println(initial);
      StdOut.println("twin board");
      StdOut.println(initial.twin());
      StdOut.println("goal board");
      StdOut.println(new Board(initial.getGoalBoard()));
      StdOut.println("neighbors");
      for (Board b : initial.neighbors()) {
        StdOut.println(b);
      }
    }
}