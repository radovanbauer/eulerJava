package euler;

import java.io.IOException;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Problem501 {

  public static void main(String[] args) throws IOException {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem501().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long n = 1_000_000_000_000L;
    FactorizationSieve sieve =
        new FactorizationSieve(Ints.checkedCast(LongMath.sqrt(n, RoundingMode.FLOOR)));
    long count = 0;
    // p^7
    for (long p = 2; LongMath.checkedPow(p, 7) <= n; p++) {
      if (sieve.isPrime(p)) {
        count++;
      }
    }
    // p1^3 * p2
    for (long p1 = 2;; p1++) {
      if (sieve.isPrime(p1)) {
        long p2Max = n / LongMath.checkedPow(p1, 3);
        long p2Count = primeCount(p2Max, sieve);
        if (p2Max >= p1) {
          p2Count--;
        }
        if (p2Count <= 0) {
          break;
        }
        count += p2Count;
      }
    }
    // p1 * p2 * p3, p1 < p2 < p3
    for (long p1 = 2; LongMath.checkedPow(p1, 3) <= n; p1++) {
      if (sieve.isPrime(p1)) {
        for (long p2 = p1 + 1; LongMath.checkedMultiply(p1, LongMath.checkedPow(p2, 2)) <= n; p2++) {
          if (sieve.isPrime(p2)) {
            long p3Min = p2 + 1;
            long p3Max = n / LongMath.checkedMultiply(p1, p2);
            if (p3Max >= p3Min) {
              long p3Count = primeCount(p3Max, sieve) - primeCount(p3Min - 1, sieve);
              count += p3Count;
            }
          }
        }
      }
    }
    return count;
  }

  private long primeCount(long n, FactorizationSieve sieve) {
    return primeCount(n, n, sieve);
  }

  @AutoValue
  static abstract class Key {
    abstract long n();
    abstract long maxp();
    static Key create(long n, long maxp) {
      return new AutoValue_Problem501_Key(n, maxp);
    }
  }

  private final Map<Key, Long> cache = new HashMap<>();

  private long primeCount(long n, long maxp, FactorizationSieve sieve) {
    if (maxp > n / maxp) {
      maxp = LongMath.sqrt(n, RoundingMode.FLOOR);
    }
    while (maxp > 1 && !sieve.isPrime(maxp)) {
      maxp--;
    }
    if (maxp <= 1) {
      return Math.max(0, n - 1);
    }
    Key key = Key.create(n, maxp);
    Long result = cache.get(key);
    if (result != null) {
      return result;
    }
    result = primeCount(n, maxp - 1, sieve) - (primeCount(n / maxp, maxp - 1, sieve) - primeCount(maxp - 1, maxp - 1, sieve));
    cache.put(key, result);
    return result;
  }
}
