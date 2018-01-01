package euler;

import static java.math.RoundingMode.FLOOR;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;

public class Problem517 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem517().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long from = 10_000_000;
    long to = 10_010_000;
    int mod = 1_000_000_007;
    FactorizationSieve sieve = new FactorizationSieve(to);
    ImmutableList<Long> primes = sieve.getPrimes(from, to);
    int maxPrime = Ints.checkedCast(Collections.max(primes));
    Binomials binomials = new Binomials(maxPrime, mod);
    return primes.stream().parallel()
        .map(prime -> calcG(Ints.checkedCast(prime), binomials, mod))
        .reduce(0L, (a, b) -> (a + b) % mod);
  }

  private long calcG(int n, Binomials binomials, int mod) {
    long sum = 0;
    int lastY = IntMath.sqrt(n, FLOOR) + 1;
    for (int x = n; 1L * (x + 1) * (x + 1) >= n; x--) {
      int y = lastY;
      if (1L * y * y * n > 1L * x * x) {
        y--;
      }
      long count = lastY == y
          ? binomials.binomialMod(n - x + y - 1, y - 1)
          : binomials.binomialMod(n - x + y, y);
      sum += count;
      lastY = y;
    }
    return sum % mod;
  }

  private static class Binomials {
    private final int mod;
    private final long[] factorial;
    private final long[] factorialInverse;

    public Binomials(int maxN, int mod) {
      this.mod = mod;
      this.factorial = new long[maxN + 1];
      this.factorialInverse = new long[maxN + 1];
      factorial[0] = 1;
      for (int i = 1; i <= maxN; i++) {
        factorial[i] = factorial[i - 1] * i % mod;
      }
      factorialInverse[maxN] = LongMod.create(factorial[maxN], mod).invert().n();
      for (int i = maxN - 1; i >= 0; i--) {
        factorialInverse[i] = factorialInverse[i + 1] * (i + 1) % mod;
      }
    }

    public long binomialMod(int n, int k) {
      return factorial[n] * factorialInverse[k] % mod * factorialInverse[n - k] % mod;
    }
  }
}
