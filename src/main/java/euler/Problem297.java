package euler;

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.Longs;

public class Problem297 {

  public static void main(String[] args) {
    System.out.println(new Problem297().solve());
  }

  public long solve() {
    long n = 100_000_000_000_000_000L;
    List<Long> fibList = new ArrayList<>();
    fibList.add(1L);
    fibList.add(2L);
    while (fibList.get(fibList.size() - 1) < n) {
      fibList.add(fibList.get(fibList.size() - 1) + fibList.get(fibList.size() - 2));
    }
    long[] fib = Longs.toArray(fibList);
    int m = fib.length;
    long[][] count = new long[m + 1][m];
    count[1][1] = 1;
    for (int i = 2; i <= m; i++) {
      count[i][1] = count[i - 1][1] + 1;
      for (int l = 2; l * 2 - 1 <= i; l++) {
        count[i][l] = count[i - 1][l] + count[i - 2][l - 1];
      }
    }
    return solve(fib, count, n);
  }

  private long solve(long[] fib, long[][] count, long max) {
    if (fib[0] >= max) {
      return 0;
    }
    int idx = 0;
    while (fib[idx + 1] < max) {
      idx++;
    }
    long sum = 0;
    for (int l = 1; l <= idx; l++) {
      sum += l * count[idx][l];
    }
    long newMax = max - fib[idx];
    sum += newMax;
    sum += solve(fib, count, newMax);
    return sum;
  }
}
