package euler;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class Problem545 {

  public static void main(String[] args) {
    System.out.println(new Problem545().solve());
  }

  public long solve() {
    int n = 100_000;
    FactorizationSieve sieve = new FactorizationSieve(500_000_000);
    int count = 0;
    List<Integer> divisors1 = sieve.divisors(308);
    Set<Long> goodPrimes = ImmutableSet.of(2L, 3L, 5L, 23L, 29L);
    long d = 0;
    outer: while (count < n) {
      d++;
      List<Integer> divisors2 = sieve.divisors(d);
      for (long d1 : divisors1) {
        for (long d2 : divisors2) {
          if (sieve.isPrime(d1 * d2 + 1) && !goodPrimes.contains(d1 * d2 + 1)) {
            continue outer;
          }
        }
      }
      count++;
      System.out.println(count + ": " + d);
    }
    return d * 308;
  }
}
