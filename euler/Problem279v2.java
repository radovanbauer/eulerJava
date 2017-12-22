package euler;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem279v2 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem279v2().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 100_000_000;
    return triples90(max) + triples60(max) + triples120(max);
  }

  private long triples90(int max) {
    long count = 0;
    for (long n = 1; 2 * (n + 1) * (n + 1) + 2 * (n + 1) * n <= max; n++) {
      for (long m = n + 1; 2 * m * m + 2 * m * n <= max; m++) {
        if ((m - n) % 2 != 0 && LongMath.gcd(m, n) == 1) {
          count += max / (2 * m * m + 2 * m * n);
        }
      }
    }
    return count;
  }

  private long triples60(int max) {
    long count = 0;
    if (max >= 1) {
      count += max / 3;
    }
    for (long n = 1; 2 * (n + 1) * (n + 1) + 5 * (n + 1) * n + 2 * n * n <= max; n++) {
      for (long m = n + 1; 2 * m * m + 5 * m * n + 2 * n * n <= max; m++) {
        if ((m - n) % 3 != 0 && LongMath.gcd(m, n) == 1) {
          count += max / (2 * m * m + 5 * m * n + 2 * n * n);
        }
      }
    }
    for (long n = 1; (2 * (n + 1) * (n + 1) + 5 * (n + 1) * n + 2 * n * n) / 3 <= max; n++) {
      for (long m = n + 1; (2 * m * m + 5 * m * n + 2 * n * n) / 3 <= max; m++) {
        if ((m - n) % 3 == 0 && LongMath.gcd(m, n) == 1) {
          count += max / ((2 * m * m + 5 * m * n + 2 * n * n) / 3);
        }
      }
    }
    return count;
  }

  private long triples120(int max) {
    long count = 0;
    for (long n = 1; 2 * (n + 1) * (n + 1) + 3 * (n + 1) * n + n * n <= max; n++) {
      for (long m = n + 1; 2 * m * m + 3 * m * n + n * n <= max; m++) {
        if ((m - n) % 3 != 0 && LongMath.gcd(m, n) == 1) {
          count += max / (2 * m * m + 3 * m * n + n * n);
        }
      }
    }
    return count;
  }
}
