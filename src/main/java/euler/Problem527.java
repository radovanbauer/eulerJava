package euler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem527 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem527().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public String solve() {
    long n = LongMath.pow(10, 10);
    double r = 1;
    double sr = 1;
    for (long k = 2; k <= n; k++) {
      r = 1 + 2 / (1D * k * k) * sr;
      sr += k * r;
    }
    return String.format("%.8f", r - b(n));
  }

  private final Map<Long, Double> bCache = new HashMap<>();

  private double b(long n) {
    if (n == 0) {
      return 0D;
    } else if (n == 1) {
      return 1D;
    } else {
      Double res = bCache.get(n);
      if (res != null) {
        return res;
      }
      long lo = (n - 1) / 2;
      long hi = n - 1 - lo;
      res = 1D + 1D * lo / n * b(lo) + 1D * hi / n * b(hi);
      bCache.put(n, res);
      return res;
    }
  }
}
