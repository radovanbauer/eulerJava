package euler;

import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem557 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem557().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long n = 10_000;
    FactorizationSieve sieve = new FactorizationSieve(n);
    AtomicInteger counter = new AtomicInteger();
    return LongStream.rangeClosed(1, n).parallel().map(a -> {
      System.out.println(counter.incrementAndGet());
      long sum = 0;
      List<Integer> aDivs = sieve.divisors(a);
      for (long d = 1; d <= n - a; d++) {
        List<Integer> dDivs = sieve.divisors(d);
        Set<Long> allDivs = new HashSet<>();
        for (long div1 : aDivs) {
          for (long div2 : aDivs) {
            for (long div3 : dDivs) {
              long b = div1 * div2 * div3;
              if (allDivs.contains(b) || a + b + d > n) {
                continue;
              }
              allDivs.add(b);
              long square =
                  4 * a * a + 4 * a * b + (b + d) * (b + d) + 4 * (a * a * d) / b + 4 * a * d;
              long sqrt = LongMath.sqrt(square, RoundingMode.FLOOR);
              if (sqrt * sqrt == square) {
                long res = sqrt - 2 * a - b - d;
                if (res % 2 == 0) {
                  long c = res / 2;
                  if (b <= c && a + b + c + d <= n) {
                    sum += a + b + c + d;
                  }
                }
              }
            }
          }
        }
      }
      return sum;
    }).sum();
  }
}
