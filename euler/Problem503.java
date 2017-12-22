package euler;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem503 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem503().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public String solve() {
    int n = 1_000_000;
    double[] f = new double[n + 1];
    f[n] = (1 + n) / 2D;
    for (int k = n - 1; k >= 1; k--) {
      double next = f[k + 1];
      for (int lo = 0; lo <= k - 1; lo++) {
        double exp = 1D * (1 + lo) * (n + 1) / (k + 1);
        if (exp < next) {
          f[k] += 1D / k * exp;
        } else {
          f[k] += 1D * (k - lo) / k * next;
          if (lo == 0) {
            return String.format("%.10f", f[k]);
          }
          break;
        }
      }
    }
    throw new RuntimeException();
  }
}
