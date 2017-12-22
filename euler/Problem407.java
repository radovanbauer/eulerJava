package euler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.common.collect.Lists;

import com.google.common.base.Stopwatch;

public class Problem407 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem407().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 10_000_000;
    ConcurrentHashMap<Integer, Integer> m = new ConcurrentHashMap<>();
    m.put(1, 0);
    for (int i = 2; i <= max; i++) {
      m.put(i, 1);
    }
    IntStream.range(2, max).parallel().forEach(a -> {
      List<Integer> divisors1 = divisors(a);
      List<Integer> divisors2 = Lists.reverse(divisors(a - 1));
      for (int d1 : divisors1) {
        for (int d2 : divisors2) {
          long prod = 1L * d1 * d2;
          if (prod <= a) {
            break;
          }
          if (prod <= max) {
            m.put((int) prod, a);
          }
        }
      }
    });
    return IntStream.rangeClosed(1, max).mapToLong(i -> m.get(i)).sum();
  }

  private List<Integer> divisors(int n) {
    List<Integer> divisors = new ArrayList<>();
    for (int d = 1; d * d <= n; d++) {
      if (n % d == 0) {
        divisors.add(d);
        if (d * d < n) {
          divisors.add(n / d);
        }
      }
    }
    Collections.sort(divisors);
    return divisors;
  }
}
