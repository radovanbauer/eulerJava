package euler;

import java.util.Arrays;

public class Problem519 {

  public static void main(String[] args) {
    Runner.run(new Problem519()::solve);
  }

  private static final int MOD = 1_000_000_000;

  public long solve() {
    return count(20000, 0);
  }

  private final long[][] cache = new long[20001][200];
  {
    for (int i = 0; i < cache.length; i++) {
      Arrays.fill(cache[i], -1);
    }
  }

  private long count(int n, int lasth) {
    if (n == 0 && lasth == 1) {
      return 1;
    }
    if (cache[n][lasth] != -1) {
      return cache[n][lasth];
    }
    long count = 0;
    long mul = 1;
    for (int h = 1; h * (h + 1) <= 2 * n; h++) {
      if (h == 1 && lasth == 0) {
        mul *= 3;
      } else if (h == 1 && lasth == 1) {
        mul *= 2;
      } else if (h == 2 && lasth < 2) {
        mul *= 2;
      }
      if (h >= lasth - 1) {
        count = (count + (mul * count(n - h, h))) % MOD;
      }
    }
    cache[n][lasth] = count;
    return count;
  }
}
