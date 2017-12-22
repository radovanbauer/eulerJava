package euler;

import java.util.Arrays;

public class Problem271 {

  public static void main(String[] args) {
    System.out.println(new Problem271().solve());
  }

  public long solve() {
    long[] primes = new long[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43 };
    long[][] roots = Arrays.stream(primes).mapToObj(this::cubicRootsOfUnity).toArray(long[][]::new);
    return solve(primes, roots, 0, 0, 1) - 1;
  }

  private long solve(long[] primes, long[][] primeRoots, long x, int k, long n) {
    if (k == primes.length) {
      System.out.println(x);
      return x;
    }
    long sum = 0;
    long newN = n * primes[k];
    for (long primeRoot : primeRoots[k]) {
      for (long newX = x; newX < newN; newX += n) {
        if (newX % primes[k] == primeRoot) {
          sum += solve(primes, primeRoots, newX, k + 1, newN);
        }
      }
    }
    return sum;
  }

  private long[] cubicRootsOfUnity(long prime) {
    if (prime == 3) {
      return new long[] { 1 };
    }
    if (prime % 3 == 2) {
      return new long[] { 1 };
    }
    if (prime % 3 == 1) {
      for (long r = 2;; r++) {
        if (r * r * r % prime == 1) {
          return new long[] { 1, r, (r * r) % prime };
        }
      }
    }
    throw new AssertionError();
  }
}
