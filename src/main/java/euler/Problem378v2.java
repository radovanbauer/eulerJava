package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem378v2 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem378v2().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 60_000_000;
    long mod = 1_000_000_000_000_000_000L;
    FactorizationSieve sieve = new FactorizationSieve(max + 1);
    int[] t = new int[max + 1];
    for (int n = 1; n <= max; n++) {
      int a = n % 2 == 0 ? n / 2 : n;
      int b = (n + 1) % 2 == 0 ? (n + 1) / 2 : n + 1;
      t[n] = sieve.divisorCount(a) * sieve.divisorCount(b);
    }
    int maxDivCount = Arrays.stream(t).skip(1).max().getAsInt();
    BinaryIndexTree before = new BinaryIndexTree(maxDivCount);
    BinaryIndexTree after = new BinaryIndexTree(maxDivCount);
    for (int i = 1; i <= max; i++) {
      after.add(t[i], 1);
    }
    LongMod res = LongMod.zero(mod);
    for (int i = 1; i <= max; i++) {
      after.add(t[i], -1);
      res = res.add(LongMod.create(before.sum(Math.min(t[i] + 1, maxDivCount), maxDivCount), mod)
          .multiply(after.sum(0, t[i] - 1)));
      before.add(t[i], 1);
    }
    return res.n();
  }

  private static class BinaryIndexTree {
    long[] t;

    public BinaryIndexTree(int maxIdx) {
      t = new long[maxIdx + 1];
    }

    public void add(int idx, long value) {
      checkArgument(idx >= 0 && idx < t.length);
      while (idx < t.length) {
        t[idx] += value;
        idx += idx & -idx;
      }
    }

    public long sum(int from, int to) {
      checkArgument(from >= 0 && from < t.length);
      checkArgument(to >= 0 && to < t.length);
      checkArgument(from <= to);
      return sum(to) - (from > 0 ? sum(from - 1) : 0);
    }

    public long sum(int to) {
      checkArgument(to >= 0 && to < t.length);
      long sum = 0;
      while (to > 0) {
        sum += t[to];
        to -= to & -to;
      }
      return sum;
    }
  }
}
