package euler;

import static java.math.RoundingMode.FLOOR;

import com.google.common.math.LongMath;

public class Problem193 {

  public static void main(String[] args) {
    System.out.println(new Problem193().solve((1L << 50) - 1));
  }

  public long solve(long n) {
    int[] primes = primes((int) (LongMath.sqrt(n, FLOOR) + 1));
    return n - countNonSquareFree(n, primes.length - 1, primes);
  }

  private long countNonSquareFree(long n, int maxPrime, int[] primes) {
    long cnt = 0L;
    for (int i = 0; i <= maxPrime; i++) {
      long primeSquare = ((long) primes[i]) * primes[i];
      if (primeSquare > n) {
        break;
      }
      cnt += n / primeSquare;
      if (i > 0) {
        cnt -= countNonSquareFree(n / primeSquare, i - 1, primes);
      }
    }
    return cnt;
  }

  private int[] primes(int n) {
    boolean[] sieve = new boolean[n];
    int cnt = 0;
    for (int d = 2; d < n; d++) {
      if (!sieve[d]) {
        cnt++;
        for (long k = ((long) d) * d; k < n; k += d) {
          sieve[(int) k] = true;
        }
      }
    }
    int[] primes = new int[cnt];
    int idx = 0;
    for (int i = 2; i < n; i++) {
      if (!sieve[i]) {
        primes[idx++] = i;
      }
    }
    return primes;
  }
}
