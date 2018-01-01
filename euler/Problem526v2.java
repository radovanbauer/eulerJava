package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;


public class Problem526v2 {

  public static void main(String[] args) {
    System.out.println(new Problem526v2().solve());
  }

  public long solve() {
    long n = 10_000_000_000_000_000L;
//    long n = 1_000_000_000;
    long sieveSize = 30_000_000;
    long segments = 100;
    long m = n;
    long max = 0;
    while (m % 30 != 11) {
      m--;
    }
    m -= sieveSize + 30;
    PrimeSieve smallPrimes = PrimeSieve.create(1, LongMath.sqrt(n + 8, RoundingMode.FLOOR));
    while (m >= 11) {
      List<Long> start = new ArrayList<>();
      while (start.size() < segments && m >= 11) {
        start.add(m);
        m = m - sieveSize + 8;
      }
      System.out.println(start);
      System.out.println(m);
      long g = start.stream().parallel()
          .mapToLong(min -> getMax(min, min + sieveSize - 1, smallPrimes))
          .max().getAsLong();
      if (g > max) {
        max = g;
        System.out.println(max);
      }
    }
    return max;
  }

  private long getMax(long min, long max, Iterable<Long> smallPrimes) {
    long res = 0;
    LargestPrimeFactorSieve sieve = new LargestPrimeFactorSieve(min, max, smallPrimes);
    for (long p = min; p <= max; p += 30) {
      if (sieve.isPrime(p) && sieve.isPrime(p + 2) && sieve.isPrime(p + 6) && sieve.isPrime(p + 8)) {
        long g = 4 * p + 16
            + sieve.largestPrimeFactor(p + 1)
            + sieve.largestPrimeFactor(p + 3)
            + sieve.largestPrimeFactor(p + 4)
            + sieve.largestPrimeFactor(p + 5)
            + sieve.largestPrimeFactor(p + 7);
        if (g > res) {
          res = g;
        }
      }
    }
    return res;
  }

  private static class LargestPrimeFactorSieve {
    private final long min, max;
    private final long[] largestPrimeFactor;

    public LargestPrimeFactorSieve(long min, long max, Iterable<Long> smallPrimes) {
      this.min = min;
      this.max = max;
      largestPrimeFactor = new long[Ints.checkedCast(max - min + 1)];
      for (long prime : smallPrimes) {
        long n = ((min - 1) / prime + 1) * prime;
        while (n <= max) {
          int idx = Ints.checkedCast(n - min);
          largestPrimeFactor[idx] = Math.max(largestPrimeFactor[idx], prime);
          n += prime;
        }
      }
      long[] product = new long[Ints.checkedCast(max - min + 1)];
      Arrays.fill(product, 1);
      for (long prime : smallPrimes) {
        long primePow = 1;
        while (primePow <= max / prime) {
          primePow *= prime;
          long n = ((min - 1) / primePow + 1) * primePow;
          while (n <= max) {
            int idx = Ints.checkedCast(n - min);
            product[idx] *= prime;
            n += primePow;
          }
        }
      }
      for (int idx = 0; min + idx <= max; idx++) {
        long n = min + idx;
        if (product[idx] < n) {
          largestPrimeFactor[idx] = n / product[idx];
        }
      }
    }

    public boolean isPrime(long n) {
      return largestPrimeFactor(n) == n;
    }

    public long largestPrimeFactor(long n) {
      checkArgument(n >= min && n <= max);
      int idx = Ints.checkedCast(n - min);
      return largestPrimeFactor[idx];
    }
  }
}
