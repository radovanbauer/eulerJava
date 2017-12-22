package euler;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem528v2 {

  private static final int MOD = 1_000_000_007;

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem528v2().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    LongMod sum = LongMod.zero(MOD);
    for (int k = 10; k <= 15; k++) {
      sum = sum.add(count(LongMath.pow(10, k), k, k));
    }
    return sum.n();
  }

  private LongMod count(long n, int k, long b) {
    return LongStream.range(0, 1L << k)
        .mapToObj(mask -> {
          long[] min = new long[k];
          for (int i = 0; i < k; i++) {
            if ((mask & (1L << i)) != 0) {
              min[i] = LongMath.pow(b, i + 1) + 1;
            }
          }
          LongMod c = count(n, min);
          return Long.bitCount(mask) % 2 == 0 ? c : c.negate();
        })
        .reduce(LongMod.zero(MOD), (x, y) -> x.add(y));
  }

  private LongMod count(long n, long[] min) {
    return binomialMod(n - Arrays.stream(min).sum() + min.length, min.length, MOD);
  }

  private LongMod binomialMod(long n, long k, long mod) {
    if (n < 0 || k < 0 || k > n) {
      return LongMod.zero(mod);
    }
    LongMod result = LongMod.create(1, mod);
    for (int i = 0; i < k; i++) {
      result = result.multiply(n - i).divide(i + 1);
    }
    return result;
  }
}
