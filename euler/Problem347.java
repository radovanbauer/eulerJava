package euler;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;

public class Problem347 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem347().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 10_000_000;
    FactorizationSieve sieve = new FactorizationSieve(max);
    boolean[] seen = new boolean[max + 1];
    long sum = 0L;
    for (int n = max; n >= 1; n--) {
      ImmutableList<Integer> primes = sieve.primeDivisors(n);
      if (primes.size() == 2) {
        int q = primes.get(0) * primes.get(1);
        if (!seen[q]) {
          sum += n;
          seen[q] = true;
        }
      }
    }
    return sum;
  }
}
