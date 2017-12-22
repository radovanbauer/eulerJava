package euler;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem512 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem512().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int n = 500_000_000;
    TotientSieve sieve = new TotientSieve(n);
    long sum = 0;
    for (int i = 1; i <= n; i += 2) {
      sum += sieve.totient(i) % (i + 1);
    }
    return sum;
  }
}
