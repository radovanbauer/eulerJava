package euler;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

import java.util.Arrays;

public class Problem581 {

  public void solve() {
    long step = 1_000_000_000L;
    long start = 1L;
    long sum = 0L;
    long[] sieve = new long[Ints.checkedCast(step)];
    while (true) {
      long end = start + step;
      sieve(sieve, start, end);
      for (int idx = 0; start + idx + 1 < end; idx++) {
        if (sieve[idx] == start + idx && sieve[idx + 1] == start + idx + 1) {
          sum += start + idx;
          System.out.println((start + idx) + " " + sum);
        }
      }
      start = end;
    }
  }

  private static final int[] primes = new int[] {2,3,5,7,11,13,17,19,23,29,31,37,41,43,47};

  private void sieve(long[] sieve, long start, long end) {
    Arrays.fill(sieve, 1);
    for (int p : primes) {
      for (long pPow = p; pPow < end; pPow *= p) {
        long idx = ((start - 1) / pPow + 1) * pPow - start;
        while (start + idx < end) {
          sieve[Ints.checkedCast(idx)] *= p;
          idx += pPow;
        }
      }
    }
  }

  private int maxPrimeDivisor(SmallFactorizationSieve sieve, long n) {
    ImmutableList<Integer> primeDivisors = sieve.primeDivisors(n);
    return primeDivisors.isEmpty() ? 1 : primeDivisors.get(primeDivisors.size() - 1);
  }

  private boolean smallDivisors(long n) {
    if (n <= 1) {
      return true;
    }
    long d = 2;
    long maxP = 0;
    for (int p : primes) {
      if (p >= n) {
        return true;
      }
      while (n % p == 0) {
        n /= p;
      }
    }
    return n == 1;
  }

  public static void main(String[] args) {
    new Problem581().solve();
  }
}
