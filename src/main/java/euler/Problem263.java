package euler;

import static com.google.common.base.Preconditions.checkArgument;

public class Problem263 {

  public static void main(String[] args) {
    System.out.println(new Problem263().solve());
  }

  public long solve() {
    int max = 2_000_000_000;
    long sum = 0;
    int found = 0;
    int tested = 0;
    Primes primes = new Primes(max);
    for (int n = 0; n <= max - 9; n += 2) {
      if (isSexy(n - 9, n - 3, primes)
          && isSexy(n - 3, n + 3, primes)
          && isSexy(n + 3, n + 9, primes)) {
        tested++;
        if (isPractical(n - 8, primes)
            && isPractical(n - 4, primes)
            && isPractical(n, primes)
            && isPractical(n + 4, primes)
            && isPractical(n + 8, primes)) {
          System.out.println(n);
          sum += n;
          found++;
          if (found == 4) {
            System.out.println("Tested " + tested);
            return sum;
          }
        }
      }
    }
    throw new IllegalStateException();
  }

  private boolean isSexy(int a, int b, Primes primes) {
    return a + 6 == b
        && primes.isPrime(a)
        && !primes.isPrime(a + 2)
        && !primes.isPrime(a + 4)
        && primes.isPrime(b);
  }

  private boolean isPractical(int n, Primes primes) {
    int sum = 1;
    while (n > 1) {
      int prime = primes.smallestPrimeDivisor(n);
      if (prime > 1 + sum) {
        return false;
      }
      int factor = 1;
      while (n % prime == 0) {
        n /= prime;
        factor *= prime;
        factor += 1;
      }
      sum *= factor;
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
