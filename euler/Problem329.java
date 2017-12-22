package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;

public class Problem329 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem329().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public BigFraction solve() {
    int max = 500;
    Primes primes = new Primes(max);
    return IntStream.rangeClosed(1, max)
        .mapToObj(pos -> BigFraction.create(1, max)
            .multiply(croakProb(primes, pos, max, "PPPPNNPPPNPPNPN")))
        .reduce(BigFraction.ZERO, (a, b) -> a.add(b));
  }

  @AutoValue
  static abstract class CacheKey {
    abstract int pos();
    abstract int maxPos();
    abstract String croaks();
    static CacheKey create(int pos, int maxPos, String croaks) {
      return new AutoValue_Problem329_CacheKey(pos, maxPos, croaks);
    }
  }

  private final Map<CacheKey, BigFraction> cache = new HashMap<>();

  private BigFraction croakProb(Primes primes, int pos, int maxPos, String croaks) {
    if (croaks.isEmpty()) {
      return BigFraction.ONE;
    }
    CacheKey key = CacheKey.create(pos, maxPos, croaks);
    if (cache.containsKey(key)) {
      return cache.get(key);
    }
    BigFraction croakProb = primes.isPrime(pos)
        ? (croaks.charAt(0) == 'P' ? BigFraction.create(2, 3) : BigFraction.create(1, 3))
        : (croaks.charAt(0) == 'P' ? BigFraction.create(1, 3) : BigFraction.create(2, 3));
    BigFraction result;
    if (pos == 1) {
      result = croakProb.multiply(croakProb(primes, pos + 1, maxPos, croaks.substring(1)));
    } else if (pos == maxPos) {
      result = croakProb.multiply(croakProb(primes, pos - 1, maxPos, croaks.substring(1)));
    } else {
      BigFraction half = BigFraction.create(1, 2);
      result = croakProb.multiply(half.multiply(
          croakProb(primes, pos + 1, maxPos, croaks.substring(1))
              .add(croakProb(primes, pos - 1, maxPos, croaks.substring(1)))));
    }
    cache.put(key, result);
    return result;
  }

  private static class Primes {
    private final int max;
    private final boolean[] nonPrimes;

    public Primes(int max) {
      this.max = max;
      nonPrimes = new boolean[max + 1];
      for (int i = 2; i <= max; i++) {
        if (!nonPrimes[i]) {
          long j = 1L * i * i;
          while (j <= max) {
            nonPrimes[(int) j] = true;
            j += i;
          }
        }
      }
    }

    public boolean isPrime(int n) {
      checkArgument(n <= max);
      return n > 1 && !nonPrimes[n];
    }
  }
}
