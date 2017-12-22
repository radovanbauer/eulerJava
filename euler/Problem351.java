package euler;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.common.base.Stopwatch;

public class Problem351 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem351().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 100_000_000;
    TotientSieve sieve = new TotientSieve(max);
    long coprime = IntStream.rangeClosed(2, max).parallel().mapToLong(sieve::totient).sum();
    return 6 * (1L * max * (max + 1) / 2 - coprime);
  }
}
