package euler;

import static com.google.common.base.Preconditions.checkArgument;

public class Problem161v3 {

  public static void main(String[] args) {
    System.out.println(new Problem161v3().solve(12, 9));
  }

  public long solve(int w, int h) {
    return count(w, new boolean[h][3]);
  }

  private Long[][] cache = new Long[13][1 << 27];

  private long count(int w, boolean[][] edge) {
    if (cache[w][pack(edge)] != null) {
      return cache[w][pack(edge)];
    }
    long res;
    int maxCol = -1;
    int maxRow = -1;
    for (int row = 0; row < edge.length; row++) {
      for (int col = 0; col < 3; col++) {
        if (edge[row][col] && col > maxCol) {
          maxCol = col;
          maxRow = row;
        }
      }
    }
    if (w == 0 && maxCol == -1) {
      res = 1L;
    } else if (w > 0 && maxCol < 2) {
      boolean[][] newEdge = new boolean[edge.length][3];
      for (int row = 0; row < edge.length; row++) {
        for (int col = 0; col < 3; col++) {
          newEdge[row][col] = col == 0 ? true : edge[row][col - 1];
        }
      }
      res = count(w - 1, newEdge);
    } else {
      res = 0L;
      for (int i = 0; i < shapes.length; i++) {
        boolean[][] shape = shapes[i];
        int row = maxRow + rowOffsets[i];
        int col = maxCol - shape[0].length + 1;
        if (fits(edge, shape, row, col)) {
          xor(edge, shape, row, col);
          res += count(w, edge);
          xor(edge, shape, row, col);
        }
      }
    }
    return cache[w][pack(edge)] = res;
  }

  private static final boolean[][][] shapes = {
    {{true, true, true}},
    {{true}, {true}, {true}},
    {{true, true}, {true, false}},
    {{true, true}, {false, true}},
    {{true, false}, {true, true}},
    {{false, true}, {true, true}},
  };

  private static final int[] rowOffsets = { 0, 0, 0, 0, -1, 0 };

  private boolean fits(
      boolean[][] grid, boolean[][] shape, int row, int col) {
    if (row < 0 ||
        col < 0 ||
        shape.length + row > grid.length ||
        shape[0].length + col > grid[0].length) {
      return false;
    }
    for (int x = 0; x < shape.length; x++) {
      for (int y = 0; y < shape[0].length; y++) {
        if (shape[x][y] && !grid[x + row][y + col]) {
          return false;
        }
      }
    }
    return true;
  }

  private void xor(boolean[][] grid, boolean[][] shape, int row, int col) {
    for (int x = 0; x < shape.length; x++) {
      for (int y = 0; y < shape[0].length; y++) {
        grid[x + row][y + col] ^= shape[x][y];
      }
    }
  }

  private int pack(boolean[][] edge) {
    int res = 0;
    for (int i = 0; i < edge.length; i++) {
      for (int j = 0; j < edge[0].length; j++) {
        if (edge[i][j]) {
          checkArgument(j < 3);
          res |= 1 << (i * 3 + j);
        }
      }
    }
    return res;
  }
}
