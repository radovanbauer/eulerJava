package euler;

import static com.google.common.base.Preconditions.checkArgument;

public class Problem357 {

  public static void main(String[] args) {
    System.out.println(new Problem357().solve());
  }

  public long solve() {
    int max = 100_000_000;
    Primes primes = new Primes(max + 1);
    long sum = 0;
    outer: for (int n = 1; n <= max; n++) {
      for (int d = 1; d * d <= n; d++) {
        if (n % d == 0) {
          if (!primes.isPrime(d + n / d)) {
            continue outer;
          }
        }
      }
      sum += n;
    }
    return sum;
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
