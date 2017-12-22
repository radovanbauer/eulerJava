package euler;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem412 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem412().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long m = 10000;
    long n = 5000;
    int mod = 76543217;
    LongMod result = fact(m * m - n * n, mod);
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < m; j++) {
        if (i >= m - n && j >= m - n) {
          continue;
        }
        long maxRight = i < m - n ? m : m - n;
        long maxDown = j < m - n ? m : m - n;
        long h = maxRight - j + maxDown - i - 1;
        result = result.divide(h);
      }
    }
    return result.n();
  }

  private LongMod fact(long n, long mod) {
    LongMod res = LongMod.create(1, mod);
    while (n > 0) {
      res = res.multiply(n);
      n--;
    }
    return res;
  }
}
