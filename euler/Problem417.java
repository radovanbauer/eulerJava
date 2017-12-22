package euler;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem417 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem417().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 100_000_000;
    Primes primes = new Primes(max);
    return IntStream.range(3, max).parallel().mapToLong(n -> length(n, primes)).sum();
  }

  private int length(int n, Primes primes) {
    while (n % 2 == 0) {
      n /= 2;
    }
    while (n % 5 == 0) {
      n /= 5;
    }
    if (n == 1) {
      return 0;
    }
    int phi = primes.phi(n);
    List<Integer> divisors = primes.divisors(phi);
    PowMod powmod = new PowMod(10, phi, n);
    for (int divisor : divisors) {
      if (powmod.powmod(divisor) == 1) {
        return divisor;
      }
    }
    throw new AssertionError();
  }

  private static class PowMod {
    private final long mod;
    private final long aPow[];

    public PowMod(long a, long maxB, long mod) {
      this.mod = mod;
      int n = LongMath.log2(maxB, RoundingMode.FLOOR) + 1;
      aPow = new long[n];
      aPow[0] = a;
      for (int i = 1; i < n; i++) {
        aPow[i] = (aPow[i - 1] * aPow[i - 1]) % mod;
      }
    }

    public long powmod(long b) {
      long res = 1;
      int i = 0;
      while (b != 0) {
        if ((b & 1) == 1) {
          res = (res * aPow[i]) % mod;
        }
        b >>= 1;
        i++;
      }
      return res;
    }
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

    public List<Integer> divisors(int n) {
      List<Integer> result = new ArrayList<>();
      result.add(1);
      divisors(n, 1, result);
      Collections.sort(result);
      return result;
    }

    private void divisors(int n, int k, List<Integer> result) {
      if (n == 1) {
        return;
      }
      int prime = smallestPrimeDivisor[n];
      int power = 0;
      while (n % prime == 0) {
        n /= prime;
        power++;
      }
      for (int i = 0; i <= power; i++) {
        if (i != 0) {
          result.add(k);
        }
        divisors(n, k, result);
        k *= prime;
      }
    }

    private int phi(int n) {
      if (n == 1) {
        return 1;
      }
      int prime = smallestPrimeDivisor[n];
      int primePower = 1;
      while (n % prime == 0) {
        primePower *= prime;
        n /= prime;
      }
      return (primePower / prime) * (prime - 1) * phi(n);
    }
  }
}
