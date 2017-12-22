package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.stream.IntStream;

import com.google.common.math.LongMath;

public class Problem268 {

  public static void main(String[] args) {
    System.out.println(new Problem268().solve());
  }

  public long solve() {
    long max = 10_000_000_000_000_000L - 1L;
    Primes primes = new Primes(100);
    int[] lowPrimes = IntStream.range(1, 100).filter(n -> primes.isPrime(n)).toArray();
    long count = 0;
    outer: for (long comb = 0; comb < (1L << lowPrimes.length); comb++) {
      int primeCount = Long.bitCount(comb);
      if (primeCount < 4) {
        continue;
      }
      long primeProduct = 1L;
      for (int i = 0; i < lowPrimes.length; i++) {
        if (((1L << i) & comb) != 0) {
          primeProduct = LongMath.checkedMultiply(primeProduct, lowPrimes[i]);
          if (primeProduct > max) {
            continue outer;
          }
        }
      }
      long factor = (primeCount % 2 == 0 ? 1 : -1) * LongMath.binomial(primeCount - 1, 3);
      count = LongMath.checkedAdd(count,  LongMath.checkedMultiply(factor, (max / primeProduct)));
    }
    return count;
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
