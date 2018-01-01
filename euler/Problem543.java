package euler;

import java.util.SortedSet;

import com.google.common.collect.ImmutableSortedSet;

public class Problem543 {

  public static void main(String[] args) {
    System.out.println(new Problem543().solve());
  }

  public long solve() {
    long sum = 0;
    int[] fib = new int[45];
    fib[0] = 0;
    fib[1] = 1;
    for (int k = 2; k <= 44; k++) {
      fib[k] = fib[k - 2] + fib[k - 1];
    }
    ImmutableSortedSet<Long> primes =
        ImmutableSortedSet.copyOf(new FactorizationSieve(fib[44]).getAllPrimes());
    for (int k = 3; k <= 44; k++) {
      sum += solve(fib[k], primes);
    }
    return sum;
  }

  private long solve(int n, SortedSet<Long> primes) {
    if (n <= 1) {
      return 0;
    } else if (n == 2) {
      return 1;
    } else if (n == 3) {
      return 2;
    } else if (n == 4) {
      return 3;
    } else {
      return primes.headSet(n + 1L).size()
          + ((n - 4) / 2 + 1)
          + (primes.headSet(n - 1L).size() - 1)
          + 1L * (n - 5 + (n - 5) % 2) * ((n - 5) / 2 + 1) / 2;
    }
  }
}
