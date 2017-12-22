package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.math.LongMath.checkedMultiply;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem365 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem365().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    FactorizationSieve sieve = new FactorizationSieve(5000);
    long[] mods = new long[5000];
    for (int p = 1001; p < 5000; p++) {
      if (sieve.isPrime(p)) {
        mods[p] = binomialMod(1_000_000_000_000_000_000L, 1_000_000_000L, p);
      }
    }
    long sum = 0;
    for (int p = 1001; p < 5000; p++) {
      if (sieve.isPrime(p)) {
        for (int q = p + 1; q < 5000; q++) {
          if (sieve.isPrime(q)) {
            for (int r = q + 1; r < 5000; r++) {
              if (sieve.isPrime(r)) {
                sum += chineseRem(new long[] {mods[p], mods[q], mods[r]}, new long[] {p, q, r});
              }
            }
          }
        }
      }
    }
    return sum;
  }

  private long chineseRem(long[] rems, long[] mods) {
    checkArgument(rems.length == mods.length && rems.length > 0);
    long N = 1L;
    for (long mod : mods) {
      N = checkedMultiply(N, mod);
    }
    LongMod res = LongMod.zero(N);
    for (int i = 0; i < rems.length; i++) {
      res = res.add(LongMod.create(rems[i], N).multiply(N / mods[i])
          .multiply(LongMod.create(N / mods[i], mods[i]).invert().n()));
    }
    return res.n();
  }

  private long binomialMod(long n, long k, int p) {
    LongMod result = LongMod.create(1, p);
    while (n > 0) {
      long np = n % p;
      long kp = k % p;
      for (int i = 0; i < kp; i++) {
        result = result.multiply(np - i).divide(i + 1);
      }
      n /= p;
      k /= p;
    }
    return result.n();
  }
}
