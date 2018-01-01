package euler;

import com.google.common.math.IntMath;

public class Problem189 {

  public static void main(String[] args) {
    System.out.println(new Problem189().solve(8));
  }

  public long solve(int n) {
    cache = new long[n + 1][IntMath.pow(3, n)];
    long cnt = 0L;
    for (int colors = 0; colors < cache[0].length; colors++) {
      cnt += solve(n, colors);
    }
    return cnt;
  }

  private long[][] cache;

  private long solve(int n, int colors) {
    if (cache[n][colors] != 0) {
      return cache[n][colors];
    }
    long res;
    if (n == 1) {
      res = 1L;
    } else {
      res = 0L;
      int maxColors = IntMath.pow(3, n - 1);
      for (int colors1 = 0; colors1 < maxColors; colors1++) {
        if (areCompatible(n - 1, colors, colors1) && areCompatible(n - 1, colors / 3, colors1)) {
          for (int colors2 = 0; colors2 < maxColors; colors2++) {
            if (areCompatible(n - 1, colors1, colors2)) {
              res += solve(n - 1, colors2);
            }
          }
        }
      }
    }
    return cache[n][colors] = res;
  }

  private boolean areCompatible(int n, int colors1, int colors2) {
    for (int i = 0; i < n; i++) {
      if (colors1 % 3 == colors2 % 3) {
        return false;
      }
      colors1 /= 3;
      colors2 /= 3;
    }
    return true;
  }
}
