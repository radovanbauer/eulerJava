package euler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem516 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem516().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  private static final long MOD = 1L << 32;

  public long solve() {
    long max = 1_000_000_000_000L;
    List<Long> hamming = new ArrayList<>();
    for (long p2 = 1; p2 <= max; p2 *= 2) {
      for (long p3 = 1; p2 * p3 <= max; p3 *= 3) {
        for (long p5 = 1; p2 * p3 * p5 <= max; p5 *= 5) {
          hamming.add(p2 * p3 * p5);
        }
      }
    }
    List<Long> singlePrimes = new ArrayList<>();
    for (long h : hamming) {
      if (isPrime(h + 1) && h + 1 > 5) {
        singlePrimes.add(h + 1);
      }
    }
    long res = 0;
    for (long h : hamming) {
      res = (res + sum(max, singlePrimes, 0, h)) % MOD;
    }
    return res;
  }

  private long sum(long max, List<Long> primes, int nextPrime, long product) {
    long res = product;
    for (int i = nextPrime; i < primes.size(); i++) {
      if (product <= max / primes.get(i)) {
        long newProduct = LongMath.checkedMultiply(product, primes.get(i));
        res = (res + sum(max, primes, i + 1, newProduct)) % MOD;
      }
    }
    return res;
  }

  private boolean isPrime(long n) {
    if (n < 2) {
      return false;
    }
    for (long d = 2; d * d <= n; d++) {
      if (n % d == 0) {
        return false;
      }
    }
    return true;
  }
}
