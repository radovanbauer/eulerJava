package euler;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem423 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem423().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 50_000_000;
    long mod = 1_000_000_007;
    FactorizationSieve sieve = new FactorizationSieve(max);
    long[] primeCount = new long[max + 1];
    for (int i = 1; i <= max; i++) {
      primeCount[i] = primeCount[i - 1] + (sieve.isPrime(i) ? 1 : 0);
    }
    LongMod sum = LongMod.zero(mod);
    int maxN = 3;
    LongMod binom = LongMod.create(1, mod); // C(3, 0)
    LongMod pow5 = LongMod.create(1, mod);
    for (int k = 0; k <= max - 1; k++) {
      binom = binom.multiply(maxN - k).divide(k + 1);
      while (maxN + 1 <= max && maxN - primeCount[maxN + 1] <= k) {
        maxN++;
        binom = binom.multiply(maxN).divide(Math.max(maxN - k - 1, 1));
      }
      pow5 = pow5.multiply(5);
      LongMod x = pow5.multiply(6).multiply(binom);
      sum = sum.add(x);
    }
    return sum.n();
  }
}
