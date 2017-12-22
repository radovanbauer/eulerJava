package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.max;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem272 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem272().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int maxPrime = 10_000_000;
    Primes primes = new Primes(maxPrime);
    int[] primes1 = IntStream.rangeClosed(2, maxPrime)
        .filter(p -> primes.isPrime(p) && (p % 3 == 1 || p == 3))
        .toArray();
    return IntStream.range(0, primes1.length).unordered().parallel()
        .mapToLong(idx -> solve(primes, primes1, primes1[idx] == 3 ? 1 : 3, idx, primes1[idx], 100_000_000_000L, 243))
        .sum();
  }

  private long solve(Primes primes, int[] primes1, int count, int lastPrimeIdx, long prod, long max, int targetCount) {
    long res = 0;
    if (count == targetCount) {
      long curProd = prod;
      for (int m = 1; curProd <= max; m++, curProd += prod) {
        if (isCountOne(primes, m)) {
          res += curProd;
        }
      }
    }
    for (int nextPrimeIdx = max(lastPrimeIdx, 0); nextPrimeIdx < primes1.length; nextPrimeIdx++) {
      int prime = primes1[nextPrimeIdx];
      if (prod > max / prime) {
        break;
      }
      int nextCount = count;
      if (prime == 3 && prod % 3 == 0 && prod % 9 != 0) {
        nextCount *= 3;
      } else if (prime != 3 && nextPrimeIdx > lastPrimeIdx) {
        nextCount *= 3;
      }
      if (nextCount > targetCount) {
        break;
      }
      long nextProd = LongMath.checkedMultiply(prod, prime);
      res += solve(primes, primes1, nextCount, nextPrimeIdx, nextProd, max, targetCount);
    }
    return res;
  }

  private boolean isCountOne(Primes primes, int n) {
    while (n > 1) {
      int prime = primes.smallestPrimeDivisor(n);
      if (prime % 3 != 2) {
        return false;
      }
      while (n % prime == 0) {
        n /= prime;
      }
    }
    return true;
  }

  private static class Primes {
    private final int max;
    private final boolean[] nonPrimes;
    private final int[] smallestPrimeDivisor;

    public Primes(int max) {
      this.max = max;
      nonPrimes = new boolean[max + 1];
      smallestPrimeDivisor = new int[max + 1];
      for (int i = 2; i <= max; i++) {
        if (!nonPrimes[i]) {
          smallestPrimeDivisor[i] = i;
          long j = 1L * i * i;
          while (j <= max) {
            if (!nonPrimes[(int) j]) {
              nonPrimes[(int) j] = true;
              smallestPrimeDivisor[(int) j] = i;
            }
            j += i;
          }
        }
      }
    }

    public boolean isPrime(int n) {
      checkArgument(n <= max);
      return n > 1 && !nonPrimes[n];
    }

    public int smallestPrimeDivisor(int n) {
      checkArgument(n >= 2 && n <= max);
      return smallestPrimeDivisor[n];
    }
  }
}
