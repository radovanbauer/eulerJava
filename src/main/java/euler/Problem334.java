package euler;

import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkState;

public class Problem334 {
  public static void main(String[] args) {
    Runner.run(new Problem334()::solve);
  }

  public long solve() {
    long t = 123456;
    int n = 1500;
    long[] b = new long[n];
    for (int i = 0; i < n; i++) {
      if (t % 2 == 0) {
        t = t / 2;
      } else {
        t = t / 2 ^ 926252;
      }
      b[i] = t % (1 << 11) + 1;
    }
    return solve(b);
  }

  private long solve(long[] list) {
    TreeMap<Long, Long> count = new TreeMap<>();
    for (int i = 0; i < list.length; i++) {
      count.put((long) i, list[i]);
    }
    long a = 0;
    long b = 0;
    long steps = 0;
    for (long lot = 0; lot <= count.lastKey(); lot++) {
      while (count.get(lot) > 1) {
        if (b == 0) {
          count.put(lot, count.get(lot) - 2);
          count.put(lot + 1, count.getOrDefault(lot + 1, 0L) + 1);
          b = a + 1;
          a = 0;
          steps++;
        } else {
          long k = Math.min(b, count.get(lot) - 1);
          count.put(lot, count.get(lot) - k);
          count.put(lot + 1, count.getOrDefault(lot + 1, 0L) + k);
          steps += (b + 1 + b - k + 2) * k / 2;
          a += k;
          b -= k;
        }
      }
      if (count.get(lot) == 0) {
        checkState(a == 0);
        a = b;
        b = 0;
      } else {
        b++;
      }
    }
    return steps;
  }
}
