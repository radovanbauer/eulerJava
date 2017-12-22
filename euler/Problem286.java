package euler;

public class Problem286 {

  public static void main(String[] args) {
    System.out.println(new Problem286().solve());
  }

  public String solve() {
    int n = 50;
    int count = 20;
    double low = n;
    double high = 2 * n;
    while (high - low > 1e-13) {
      double mid = (low + high) / 2;
      double[][] p = new double[n + 1][count + 1];
      p[0][0] = 1;
      for (int i = 1; i <= n; i++) {
        for (int j = 0; j <= count; j++) {
          p[i][j] = 1D * i / mid * p[i - 1][j];
          if (j > 0) {
            p[i][j] += (1 - 1D * i / mid) * p[i - 1][j - 1];
          }
        }
      }
      if (p[n][count] < 0.02) {
        high = mid;
      } else {
        low = mid;
      }
    }
    return String.format("%.10f", low);
  }
}
