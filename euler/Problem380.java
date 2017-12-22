package euler;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

public class Problem380 {
  public static void main(String[] args) {
    Runner.run(new Problem380()::solve);
  }

  public BigDecimal solve() {
    int n = 100;
    int m = 500;
    MathContext ctx = MathContext.DECIMAL128;
    List<Map<Integer, BigDecimal>> matrix = new ArrayList<>();
    for (int i = 0; i < n * m; i++) {
      matrix.add(new HashMap<>());
    }
    int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    for (int x = 0; x < n; x++) {
      for (int y = 0; y < m; y++) {
        int neighbours = 0;
        for (int[] dir : dirs) {
          int nx = x + dir[0];
          int ny = y + dir[1];
          if (nx >= 0 && nx < n && ny >= 0 && ny < m) {
            neighbours++;
            matrix.get(node(n, m, x, y)).put(node(n, m, nx, ny), BigDecimal.valueOf(-1));
          }
        }
        matrix.get(node(n, m, x, y)).put(node(n, m, x, y), BigDecimal.valueOf(neighbours));
      }
    }
    matrix.remove(n * m - 1);
    matrix.forEach(row -> row.remove(n * m - 1));
    System.out.println(matrix);
    int N = n * m - 1;

    // gauss
    BigDecimal result = BigDecimal.ONE;
    for (int x1 = 0; x1 < N; x1++) {
      int nonZeroX = x1;
      while (nonZeroX < N && !matrix.get(nonZeroX).containsKey(x1)) {
        nonZeroX++;
      }
      checkState(nonZeroX < N && matrix.get(nonZeroX).containsKey(x1));
      Map<Integer, BigDecimal> tmp = matrix.get(x1);
      matrix.set(x1, matrix.get(nonZeroX));
      matrix.set(nonZeroX, tmp);
      Map<Integer, BigDecimal> row1 = matrix.get(x1);

      for (int x2 = x1 + 1; x2 < N; x2++) {
        Map<Integer, BigDecimal> row2 = matrix.get(x2);
        if (row2.containsKey(x1)) {
          BigDecimal ratio = row2.get(x1).divide(row1.get(x1), ctx);
          for (int y : row1.keySet()) {
            BigDecimal newElem = row2.getOrDefault(y, BigDecimal.ZERO).subtract(row1.get(y).multiply(ratio, ctx), ctx);
            if (newElem.abs().compareTo(BigDecimal.valueOf(1, 10)) > 0) {
              row2.put(y, newElem);
            } else {
              row2.remove(y);
            }
          }
        }
      }
      result = result.multiply(row1.get(x1), ctx);
      System.out.println(x1 + ": " + row1.get(x1));
      matrix.set(x1, null);
    }

    return result;
  }

  private int node(int n, int m, int x, int y) {
    return x * m + y;
  }
}
