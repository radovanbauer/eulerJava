package euler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Problem399 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem399().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public String solve() {
    int n = 100_000_000;
    int maxp = n;
    FactorizationSieve sieve = new FactorizationSieve(maxp);
    Set<Integer> squarePeriodsSet = new HashSet<>();
    int maxRes = 3 * n / 2;
    for (int p = 2; p <= maxp; p++) {
      if (sieve.isPrime(p)) {
        long i = 1;
        long f0 = 0;
        long f1 = 1;
        while (f1 % p != 0 && i * p <= maxRes) {
          i++;
          long f = (f0 + f1) % p;
          f0 = f1;
          f1 = f;
        }
        if (i * p <= maxRes) {
          squarePeriodsSet.add(Ints.checkedCast(i * p));
        }
      }
    }
    boolean[] fSieve = new boolean[maxRes + 1];
    for (int period : squarePeriodsSet) {
      for (int i = period; i <= maxRes; i += period) {
        fSieve[i] = true;
      }
    }
    int count = 0;
    int i = 0;
    while (count < n) {
      i++;
      if (!fSieve[i]) {
        count++;
      }
    }
    long last16 = fibMod(i, LongMath.pow(10, 16));
    double phi = (1 + Math.sqrt(5)) / 2;
    double log = i * Math.log10(phi) - Math.log10(5) / 2;
    int exp = (int) log;
    double mantissa = Math.pow(10, log - exp);
    return String.format("%d,%.1fe%d", last16, mantissa, exp);
  }

  private long fibMod(long n, long mod) {
    return BigModMatrix.create(new long[][] {{1, 1}, {1, 0}}, mod).pow(n)
        .element(1, 0).n().longValueExact();
  }
}
