package euler;

import com.google.common.math.LongMath;

import java.util.Arrays;

public class Problem349 {

  public static void main(String[] args) {
    Runner.run(new Problem349()::solve);
  }

  public long solve() {
    char[][] grid = new char[200][200];
    long max = LongMath.pow(10, 18);
    Arrays.stream(grid).forEach(row -> Arrays.fill(row, '.'));
    int[][] dirs = new int[][] {{1, 0}, {0, -1}, {-1, 0}, {0, 1}};
    long[] a = new long[20000];
    int x = 100;
    int y = 100;
    int dirIdx = 0;
    int blacks = 0;
    for (int step = 1; step < 9977 + 104; step++) {
      if (grid[x][y] == 'X') {
        grid[x][y] = '.';
        blacks--;
        dirIdx = (dirIdx + 3) % 4;
      } else {
        grid[x][y] = 'X';
        blacks++;
        dirIdx = (dirIdx + 1) % 4;
      }
      x += dirs[dirIdx][0];
      y += dirs[dirIdx][1];
      a[step] = blacks;
    }

    int pos = 9977 + LongMath.mod(max - 9977, 104);
    return a[pos] + (max - pos) / 104 * 12;
  }
}
