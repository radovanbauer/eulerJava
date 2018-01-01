package euler;

import com.google.common.primitives.Ints;

public class Problem341 {

  public static void main(String[] args) {
    System.out.println(new Problem341().solve());
  }

  public long solve() {
    long max = 1_000_000;
    int[] g = new int[20_000_000];
    g[1] = 1;
    g[2] = 2;
    g[3] = 2;
    long m = 4;
    long o = 6;
    long c = 2;
    long sum = 1;
    for (int n = 3; c < max; n++) {
      long mStart = m;
      for (long i = m; i < mStart + g[n]; i++) {
        if (i < g.length) {
          g[Ints.checkedCast(i)] = n;
        }
        o += n;
        while (c * c * c < o && c < max) {
          sum += m;
          c++;
        }
        m++;
      }
    }
    return sum;
  }
}
