package euler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

import java.util.List;
import java.util.Set;

public class Problem204v2 {

  public static void main(String[] args) {
    System.out.println(new Problem204v2().solve(1000000000, 100));
  }

  public int solve(int maxn, int maxp) {
    int[] primes = primes(maxp + 1);
    Set<Integer> numbers = Sets.newHashSet();
    numbers.add(1);
    for (int prime : primes) {
      int added;
      do {
        added = 0;
        for (int num : ImmutableList.copyOf(numbers)) {
          if (((long) num) * prime <= maxn && !numbers.contains(num * prime)) {
            numbers.add(num * prime);
            added++;
          }
        }
      } while (added > 0);
    }
    return numbers.size();
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
