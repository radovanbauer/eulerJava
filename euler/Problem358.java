package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigInteger;

public class Problem358 {

  public static void main(String[] args) {
    System.out.println(new Problem358().solve());
  }

  public long solve() {
    Primes primes = new Primes(1_000_000_000);
    for (int n = 100_000_000;; n++) {
      if (primes.isPrime(n)) {
        if ((99999L - 56789L * n) % 100_000L == 0) {
          if (99999999999L / n == 137) {
            long rem = 0;
            long sum = 0;
            for (int i = 0; i < n - 1; i++) {
              rem *= 10;
              rem += 9;
              long div = rem / n;
              rem -= div * n;
              sum += div;
            }
            System.out.println(n + " " + sum);
          }
        }
      }
    }
  }

  private int sumOfDigits(BigInteger bigInteger) {
    return bigInteger.toString().chars().map(c -> c - '0').sum();
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
            if (!nonPrimes[(int) j]) {
              nonPrimes[(int) j] = true;
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
  }
}
