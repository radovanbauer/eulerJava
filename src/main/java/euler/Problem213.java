package euler;



public class Problem213 {

  public static void main(String[] args) {
    System.out.println(new Problem213().solve(30, 50));
  }

  public String solve(int n, int rounds) {
    double[][] pNone = new double[n][n];
    for (int x = 0; x < n; x++) {
      for (int y = 0; y < n; y++) {
        pNone[x][y] = 1D;
      }
    }
    for (int startX = 0; startX < n; startX++) {
      for (int startY = 0; startY < n; startY++) {
        double[][][] p = new double[rounds + 1][n][n];
        p[0][startX][startY] = 1;
        for (int round = 1; round <= rounds; round++) {
          for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
              int options = (x > 0 ? 1 : 0) + (y > 0 ? 1 : 0)
                  + (x < n - 1 ? 1 : 0) + (y < n - 1 ? 1 : 0);
              if (x > 0) {
                p[round][x - 1][y] += p[round - 1][x][y] / options;
              }
              if (x < n - 1) {
                p[round][x + 1][y] += p[round - 1][x][y] / options;
              }
              if (y > 0) {
                p[round][x][y - 1] += p[round - 1][x][y] / options;
              }
              if (y < n - 1) {
                p[round][x][y + 1] += p[round - 1][x][y] / options;
              }
            }
          }
        }
        for (int x = 0; x < n; x++) {
          for (int y = 0; y < n; y++) {
            pNone[x][y] *= 1 - p[rounds][x][y];
          }
        }
      }
    }
    double expected = 0D;
    for (int x = 0; x < n; x++) {
      for (int y = 0; y < n; y++) {
        expected += pNone[x][y];
      }
    }
    return String.format("%.6f", expected);
  }
}
