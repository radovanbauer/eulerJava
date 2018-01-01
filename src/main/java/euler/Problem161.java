package euler;

public class Problem161 {

  public static void main(String[] args) {
    System.out.println(new Problem161().solve(9, 12));
  }

  public long solve(int n, int m) {
    return solve(new boolean[n][m], 0, 0);
  }

  private static final int[][][] PATTERNS = {
    {{0, 0}, {0, 1}, {0, 2}},
    {{0, 0}, {1, 0}, {2, 0}},
    {{0, 0}, {0, 1}, {1, 0}},
    {{0, 0}, {0, 1}, {1, 1}},
    {{0, 0}, {1, 0}, {1, 1}},
    {{0, 0}, {1, 0}, {1, -1}},
  };

  private long solve(boolean[][] grid, int x, int y) {
//    print(grid);   
    while (grid[x][y]) {
      y++;
      if (y == grid[0].length) {
        y = 0;
        x++;
        if (x == grid.length) {
          return 1L;
        }
      }
    }
    long sum = 0L;
    for (int[][] pattern : PATTERNS) {
      if (!fits(grid, pattern, x, y)) {
        continue;
      }
      for (int[] point : pattern) {
        grid[x + point[0]][y + point[1]] = true;
      }
      sum += solve(grid, x, y);
      for (int[] point : pattern) {
        grid[x + point[0]][y + point[1]] = false;
      }
    }
    return sum;
  }

  private boolean fits(boolean[][] grid, int[][] pattern, int x, int y) {
    for (int[] point : pattern) {
      if (!inside(grid, x + point[0], y + point[1]) || grid[x + point[0]][y + point[1]]) {
        return false;
      }
    }
    return true;
  }

  private boolean inside(boolean[][] grid, int x, int y) {
    return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
  }

  private void print(boolean[][] grid) {
    for (int x = 0; x < grid.length; x++) {
      for (int y = 0; y < grid[x].length; y++) {
        System.out.print(grid[x][y] ? 'X' : '.');
      }
      System.out.println();
    }
    System.out.println();
  }
}
