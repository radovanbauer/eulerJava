package euler;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem510 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem510().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int n = 1_000_000_000;
    long sum = (9L * (n / 4) * (1 + n / 4) / 2);
    for (long asqrt = 1; asqrt * asqrt <= n; asqrt++) {
      long a = asqrt * asqrt;
      for (long bsqrt = asqrt + 1; bsqrt * bsqrt <= n; bsqrt++) {
        long b = bsqrt * bsqrt;
        long x = a * b;
        long y = a + b + 2 * asqrt * bsqrt;
        if (x % y == 0) {
          long c = x / y;
          if (LongMath.gcd(LongMath.gcd(a, b), c) == 1) {
            long maxk = n / b;
            sum += (a + b + c) * maxk * (maxk + 1) / 2;
          }
        }
      }
    }
    return sum;
  }
}
