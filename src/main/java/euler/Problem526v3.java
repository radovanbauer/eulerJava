package euler;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;


public class Problem526v3 {

  public static void main(String[] args) {
    System.out.println(new Problem526v3().solve());
  }

  public long solve() {
    long n = 9999294715100880L;
//    long n = 1_000_000_000;
    long sieveSize = 210 * 12_000_000L;
    long segments = 24;
    long m = n;
    long max = 0;
    while (m % 210 != 0) {
      m--;
    }
    m -= sieveSize - 210;
    long[] smallPrimes = Longs.toArray(ImmutableList.copyOf(PrimeSieve.create(1, LongMath.sqrt(n + 8, RoundingMode.FLOOR))));
    long[] smallPrimesInv210 = getInv(smallPrimes, 210);
    long[] smallPrimesInv35 = getInv(smallPrimes, 35);
    while (m >= 0) {
      Stopwatch stopwatch = Stopwatch.createStarted();
      List<Long> start = new ArrayList<>();
      while (start.size() < segments && m >= 0) {
        start.add(m);
        m = m - sieveSize;
      }
      long g = start.stream().parallel()
          .mapToLong(min -> getMax2(min, min + sieveSize, smallPrimes, smallPrimesInv210, smallPrimesInv35))
          .max().getAsLong();
      if (g > max) {
        max = g;
        System.out.println(max);
      }
      long elapsedMillis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
      double ratebps = start.size() * sieveSize / (elapsedMillis / 1000D) / 1e9;
      System.out.printf("ratebps=%.3f m=%d\n", ratebps, m);
    }
    return max;
  }

  private long[] getInv(long[] smallPrimes, int mod) {
    return Arrays.stream(smallPrimes).map(p -> {
      if (LongMath.gcd(p, mod) == 1) {
        return LongMod.create(p, mod).invert().n();
      } else {
        return 0;
      }
    }).toArray();
  }

  private long getMax(long min, long max, long[] smallPrimes) {
    checkArgument(min % 210 == 0 && max % 210 == 0);
    long res = 0;
    BigSetPrimeSieve sieve = new BigSetPrimeSieve(min, max, smallPrimes);
    for (long p = min; p < max; p += 210) {
      if (sieve.isPrime(p + 101) && sieve.isPrime(p + 103) && sieve.isPrime(p + 107) && sieve.isPrime(p + 109)) {
        long g =
            (p + 101)
            + largestPrimeFactor(p + 102, smallPrimes)
            + (p + 103)
            + largestPrimeFactor(p + 104, smallPrimes)
            + largestPrimeFactor(p + 105, smallPrimes)
            + largestPrimeFactor(p + 106, smallPrimes)
            + (p + 107)
            + largestPrimeFactor(p + 108, smallPrimes)
            + (p + 109);
        if (g > res) {
          System.out.println(p + ": " + g);
          res = g;
        }
      }
    }
    return res;
  }

  private long getMax2(long min, long max, long[] smallPrimes, long[] smallPrimesInv210, long[] smallPrimesInv35) {
    checkArgument(min % 210 == 0 && max % 210 == 0);
    long res = 0;
    ModBigSetPrimeSieve sieve101 = new ModBigSetPrimeSieve(min, max, 210, 101, smallPrimes, smallPrimesInv210);
    ModBigSetPrimeSieve sieve103 = new ModBigSetPrimeSieve(min, max, 210, 103, smallPrimes, smallPrimesInv210);
    ModBigSetPrimeSieve sieve107 = new ModBigSetPrimeSieve(min, max, 210, 107, smallPrimes, smallPrimesInv210);
    ModBigSetPrimeSieve sieve109 = new ModBigSetPrimeSieve(min, max, 210, 109, smallPrimes, smallPrimesInv210);
    ModBigSetPrimeSieve sieve102 = new ModBigSetPrimeSieve(min / 6, max / 6, 35, 17, smallPrimes, smallPrimesInv35);
    ModBigSetPrimeSieve sieve108 = new ModBigSetPrimeSieve(min / 6, max / 6, 35, 18, smallPrimes, smallPrimesInv35);
    int idx = 0;
    for (long p = min; p < max; p += 210, idx++) {
      if (sieve101.isPrime(idx) && sieve103.isPrime(idx) && sieve107.isPrime(idx) && sieve109.isPrime(idx)
          && (sieve102.isPrime(idx) || sieve108.isPrime(idx))) {
//        checkState(largestPrimeFactor(p + 101, smallPrimes) == p + 101);
//        checkState(largestPrimeFactor(p + 103, smallPrimes) == p + 103);
//        checkState(largestPrimeFactor(p + 107, smallPrimes) == p + 107);
//        checkState(largestPrimeFactor(p + 109, smallPrimes) == p + 109);
        long g =
            (p + 101)
                + largestPrimeFactor((p + 102) / 6, smallPrimes)
                + (p + 103)
                + largestPrimeFactor((p + 104) / 2, smallPrimes)
                + largestPrimeFactor((p + 105) / (3 * 5 * 7), smallPrimes)
                + largestPrimeFactor((p + 106) / 2, smallPrimes)
                + (p + 107)
                + largestPrimeFactor((p + 108) / 6, smallPrimes)
                + (p + 109);
        if (g > res) {
          res = g;
        }
      }
    }
    return res;
  }

  private long largestPrimeFactor(long n, long[] smallPrimes) {
    for (long prime : smallPrimes) {
      if (prime * prime > n) {
        return n;
      }
      while (n % prime == 0) {
        n /= prime;
      }
      if (n == 1) {
        return prime;
      }
    }
    return n;
  }

  private static class BigSetPrimeSieve {
    private final long min, max;
    private final BitSet nonPrime;

    public BigSetPrimeSieve(long min, long max, long[] smallPrimes) {
      this.min = min;
      this.max = max;
      int length = Ints.checkedCast(max - min + 1);
      nonPrime = new BitSet(length);
      for (long prime : smallPrimes) {
        int n = Ints.checkedCast(((min - 1) / prime + 1) * prime - min);
        while (n <= length) {
          nonPrime.set(n, true);
          n += prime;
        }
      }
    }

    public boolean isPrime(long n) {
      checkArgument(n >= min && n <= max);
      int idx = Ints.checkedCast(n - min);
      return !nonPrime.get(idx);
    }
  }


  private static class ModBigSetPrimeSieve {
    private final long min, max, mod, val;
    private final BitSet nonPrime;
    private final int length;

    public ModBigSetPrimeSieve(long min, long max, long mod, long val, long[] smallPrimes, long[] smallPrimesInv210) {
      this.min = min;
      this.max = max;
      this.mod = mod;
      this.val = val;
      checkArgument(val >= 0 && val < mod);
      checkArgument(LongMath.gcd(val, mod) == 1);
      long start = min;
      while (start % mod != val) {
        start++;
      }
      length = Ints.checkedCast((max - start) / mod + 1);
      nonPrime = new BitSet(length);
      for (int pi = 0; pi < smallPrimes.length; pi++) {
        long prime = smallPrimes[pi];
        long primeInv = smallPrimesInv210[pi];
        // k * prime == val (mod mod)
        // k == prime^-1 * val (mod mod)
        //
        // (l * mod + k) * prime >= start
        // l * mod * prime >= start - k * prime
        // l >= (start - k * prime) / (mod * prime)
        if (primeInv == 0) {
          continue;
        }
        long k = (primeInv * val) % mod;
        long minl = Math.max((start - k * prime + mod * prime - 1) / (mod * prime), 0);
        long n = (minl * mod + k) * prime;
        int idx = Ints.checkedCast((n - start) / mod);
        while (idx < length) {
          nonPrime.set(idx, true);
          idx += prime;
        }
      }
    }

    public boolean isPrime(int idx) {
      checkArgument(idx < length);
      return !nonPrime.get(idx);
    }
  }
}
