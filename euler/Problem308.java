package euler;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem308 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem308().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int primeCount = 10001;
    long iterations = 0;
    long prevHighestDivisor = 1;
    for (long n = 2;; n++) {
      iterations += n * 3 - 4 + prevHighestDivisor;
      for (long d = n - 1; d > 0; d--) {
        if (n % d != 0) {
          iterations += 6 * n + 2 + 2 * (n / d);
        } else {
          iterations += 4 * n + 4 + 2 * (n / d);
          prevHighestDivisor = d;
          if (d == 1) {
            primeCount--;
            if (primeCount == 0) {
              return iterations;
            }
          }
          break;
        }
      }
    }
  }
}
