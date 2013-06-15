package euler;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import java.util.List;

public class Problem204 {

  public static void main(String[] args) {
    System.out.println(new Problem204().solve(1000000000, 100));
  }

  public int solve(int maxn, int maxp) {
    int[] primes = primes(maxp + 1);
    int cnt = 0;
    for (int i = 1; i <= maxn; i++) {
      if (i % 1000000 == 0) System.out.println(i);
      int n = i;
      for (int p = 0; p < primes.length && n > 1; p++) {
        while (n % primes[p] == 0) {
          n /= primes[p];
        }
      }
      if (n == 1) {
        cnt++;
      }
    }
    return cnt;
  }

  private int[] primes(int n) {
    boolean[] sieve = new boolean[n];
    List<Integer> res = Lists.newArrayList();
    for (int d = 2; d < n; d++) {
      if (!sieve[d]) {
        res.add(d);
        long k = 1L * d * d;
        while (k < n) {
          sieve[(int) k] = true;
          k += d;
        }
      }
    }
    return Ints.toArray(res);
  }
}
