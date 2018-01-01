package euler;

import com.google.common.collect.ImmutableList;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Random;


public class Problem526 {

  public static void main(String[] args) {
    Runner.run(new Problem526()::solve);
//    Runner.run(new Problem526()::solve2);
  }

  public long solve() {
    int certainty = 1;
    long n = LongMath.pow(10, 16);
//    long n = 1_000_000_000;
    long max = 1L;
    long m = n;
    while (m % 210 != 0) {
      m--;
    }
    long[] smallPrimes = Longs.toArray(ImmutableList.copyOf(
        PrimeSieve.create(1, LongMath.sqrt(n, RoundingMode.FLOOR))));
    for (; m >= 0; m -= 210) {
      if (isPrime(m + 101, certainty)
//          && BigInteger.valueOf((m + 102) / (2 * 3)).isProbablePrime(10)
          && isPrime(m + 103, certainty)
          && isPrime((m + 104) % 4 == 0 ? (m + 104) / 4 : (m + 104) / 2, certainty)
//          && BigInteger.valueOf((m + 105) / (3 * 5 * 7)).isProbablePrime(10)
          && isPrime((m + 106) % 4 == 0 ? (m + 106) / 4 : (m + 106) / 2, certainty)
          && isPrime(m + 107, certainty)
//          && BigInteger.valueOf((m + 108) / (2 * 3)).isProbablePrime(10)
          && isPrime(m + 109, certainty)) {
        long g = 0
            + largestPrimeFactor(m + 101, smallPrimes)
            + largestPrimeFactor(m + 102, smallPrimes)
            + largestPrimeFactor(m + 103, smallPrimes)
            + largestPrimeFactor(m + 104, smallPrimes)
            + largestPrimeFactor(m + 105, smallPrimes)
            + largestPrimeFactor(m + 106, smallPrimes)
            + largestPrimeFactor(m + 107, smallPrimes)
            + largestPrimeFactor(m + 108, smallPrimes)
            + largestPrimeFactor(m + 109, smallPrimes);
        if (g > max) {
          max = g;
          System.out.println(m + ": " + g + " max: " + max);
        }
      }
    }
    return max;
  }

  public long solve2() {
//    long n = 100;
    long n = LongMath.pow(10, 9);
    int segmentSize = IntMath.pow(10, 8) * 5;
    long end = n + 8;
    long[] smallPrimes = Longs.toArray(ImmutableList.copyOf(
        PrimeSieve.create(1, LongMath.sqrt(end, RoundingMode.FLOOR))));
    long max = 0;
    while (end >= 2) {
      long start = Math.max(end - segmentSize + 1, 2);
      System.out.printf("start=%d end=%d\n", start, end);
      long[] f = new long[Ints.checkedCast(end - start + 1)];
      long[] prod = new long[f.length];
      Arrays.fill(prod, 1);
      for (long prime : smallPrimes) {
        long primePow = 1;
        while (primePow <= end / prime) {
          primePow *= prime;
          long primeMul = ((start - 1) / primePow + 1) * primePow;
          while (primeMul <= end) {
            f[Ints.checkedCast(primeMul - start)] = prime;
            prod[Ints.checkedCast(primeMul - start)] *= prime;
            primeMul += primePow;
          }
        }
      }
      for (int i = 0; i < f.length; i++) {
        if (prod[i] != start + i) {
          f[i] = (start + i) / prod[i];
        }
      }
      long m = end - 8;
      while (LongMath.mod(m, 30) != 11) {
        m--;
      }
      int cnt = 0;
      for (int idx = Ints.checkedCast(m - start); idx >= 0; idx -= 30, cnt++) {
        long g = f[idx] + f[idx + 1] + f[idx + 2] + f[idx + 3] + f[idx + 4] + f[idx + 5] + f[idx + 6] + f[idx + 7] + f[idx + 8];
        if (g > max) {
          System.out.println((start + idx) + ": " + g);
          max = g;
        }
//        if ((cnt & 0xFFFF) == 0) {
////          System.out.println(idx);
//        }
      }
      end = end - segmentSize + 8;
    }
    return max;
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

  private boolean isPrime(long n, int iteration) {
    return BigInteger.valueOf(n).isProbablePrime(iteration);
  }

  private final Random rand = new Random();

  private boolean isPrime2(long n, int iteration) {
    /** base case **/
    if (n == 0 || n == 1)
      return false;
    /** base case - 2 is prime **/
    if (n == 2)
      return true;
    /** an even number other than 2 is composite **/
    if (n % 2 == 0)
      return false;

    for (int i = 0; i < iteration; i++)
    {
      long r = Math.abs(rand.nextLong());
      long a = r % (n - 1) + 1;
      if (modPow(BigInteger.valueOf(a), n - 1, BigInteger.valueOf(n)).longValueExact() != 1) {
        return false;
      }
    }
    return true;
  }

  private BigInteger modPow(BigInteger n, long pow, BigInteger m) {
    if (pow == 1) {
      return n;
    }
    BigInteger x = modPow(n, pow >> 1, m);
    BigInteger xsq = x.multiply(x).mod(m);
    if ((pow & 1) == 0) {
      return xsq;
    } else {
      return xsq.multiply(n).mod(m);
    }
  }

  private BigInteger modPow2(BigInteger n, long pow, BigInteger m) {
    return n.modPow(BigInteger.valueOf(pow), m);
  }
}
