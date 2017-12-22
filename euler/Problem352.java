package euler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem352 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem352().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public String solve() {
    List<Double> p = new ArrayList<>();
    for (double i = 1; i <= 50; i++) {
      p.add(i * 0.01D);
    }
    return String.format("%.6f", p.stream().parallel().mapToDouble(x -> solve(10_000, x)).sum());
  }

  private double solve(int maxn, double p) {
    double[] q = new double[maxn + 1];
    q[0] = 1D;
    for (int i = 1; i <= maxn; i++) {
      q[i] = q[i - 1] * (1 - p);
    }
    double[] t = new double[maxn + 1];
    double[] s = new double[maxn + 1];
    for (int n = 1; n <= maxn; n++) {
      if (n == 1) {
        s[1] = 0;
      } else {
        s[n] = Double.MAX_VALUE;
        for (int i = 1; i <= n - 1; i++) {
          double pi = 1
              + q[i] * (1 - q[n - i]) / (1 - q[n]) * s[n - i]
              + (1 - q[i]) / (1 - q[n]) * (s[i] + t[n - i]);
          s[n] = Math.min(s[n], pi);
        }
      }
      t[n] = 1 + (1 - q[n]) * s[n];
      for (int i = 1; i <= n - 1; i++) {
        t[n] = Math.min(t[n], t[i] + t[n - i]);
      }
    }
    return t[maxn];
  }
}
