package euler;

import java.math.RoundingMode;
import java.util.stream.LongStream;

import com.google.common.math.LongMath;
import com.google.common.primitives.Longs;

public class Problem540 {

  public static void main(String[] args) {
    System.out.println(new Problem540().solve());
  }

  public long solve() {
    long max = 3141592653589793L;
    FactorizationSieve sieve = new FactorizationSieve(LongMath.sqrt(max, RoundingMode.FLOOR));
    return LongStream.rangeClosed(1, LongMath.sqrt(max, RoundingMode.FLOOR)).parallel().map(m -> {
      long nmax = Math.min(m - 1, LongMath.sqrt(max - m * m, RoundingMode.FLOOR));
      if (m % 2 == 0) {
        return coprimeCount(m, nmax, sieve);
      } else {
        return coprimeCount(m, nmax / 2, sieve);
      }
    }).sum();
  }

  private long coprimeCount(long n, long max, FactorizationSieve sieve) {
    return max - divCount(n, max, Longs.toArray(sieve.primeDivisors(n)), 0, 1, 0);
  }

  private long divCount(long n, long max, long[] primes, int nextPrime, long product, int primeCount) {
    long count = 0;
    for (int next = nextPrime; next < primes.length; next++) {
      long newProduct = product * primes[next];
      int newPrimeCount = primeCount + 1;
      int sign = newPrimeCount % 2 == 1 ? 1 : -1;
      count += sign * max / newProduct;
      count += divCount(n, max, primes, next + 1, newProduct, newPrimeCount);
    }
    return count;
  }
}
