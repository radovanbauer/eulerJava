package euler;

import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

import java.math.RoundingMode;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkState;

public class Problem420 {
  public static void main(String[] args) {
    Runner.run(new Problem420()::solve);
  }

  public long solve() {
    int max = 10_000_000;
    long count = 0;
    long maxBc = 0;
    FactorizationSieve sieve = new FactorizationSieve(max);
    int[] divisors = IntStream.rangeClosed(0, max).map(i -> i == 0 ? 0 : sieve.divisorCount(i)).toArray();
    for (long t1 = 1; t1 * t1 <= max; t1++) {
      System.out.println(t1);
      for (long t2 = t1 + 2; t2 * t2 <= 2 * max; t2 += 2) {
        checkState((t2 * t2 - t1 * t1) % 4 == 0);
        long s = (t2 * t2 - t1 * t1) / 4;
        long tr = t1 * t1 + 2 * s;
        if (tr >= max) {
          continue;
        }
        long t1t2lcm = t1 / LongMath.gcd(t1, t2) * t2;
        for (long A = t1 + s; A < tr; A += t1) {
          if ((A + s) % t2 != 0) {
            continue;
          }
          long D = tr - A;
          if (D <= s || (D - s) % t1 != 0 || (D + s) % t2 != 0) {
            continue;
          }
//          System.out.printf("t1=%d t2=%d tr=%d s=%d\n", t1, t2, tr, s);
          long BC = A * D - s * s;
          int bc = Ints.checkedCast(LongMath.divide(BC, (t1t2lcm * t1t2lcm), RoundingMode.UNNECESSARY));
          maxBc = Math.max(maxBc, bc);
          count += divisors[bc];
        }
      }
    }
    return count;
  }
}
