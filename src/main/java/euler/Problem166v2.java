package euler;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem166v2 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createUnstarted();
    stopwatch.start();
    System.out.println(new Problem166v2().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
  }

  public long solve() {
    // a b c d
    // e f g h
    // i j k l
    // m n o p
    long cnt = 0L;
    for (int a = 0; a <= 9; a++) {
      for (int b = 0; b <= 9; b++) {
        for (int c = 0; c <= 9; c++) {
          for (int d = 0; d <= 9; d++) {
            int sum = a + b + c + d;
            int emin = max(0, sum - 18 - a);
            int emax = min(9, sum - a);
            for (int e = emin; e <= emax; e++) {
              int fmin = max(max(max(0, sum - 18 - b), sum - 18 - e), sum - 18 - a);
              int fmax = min(min(min(9, sum - b), sum - e), sum - a);
              for (int f = fmin; f <= fmax; f++) {
                int gmin = max(max(0, sum - 9 - e - f), sum - 18 - c);
                int gmax = min(min(9, sum - e - f), sum - c);
                for (int g = gmin; g <= gmax; g++) {
                  // int h = sum - e - f - g;
                  int imin = max(max(max(0, sum - 9 - a - e), g + d - a - e), sum - a - b + d - e - f + g - 9);
                  int imax = min(min(min(9, sum - a - e), 9 + g + d - a - e), sum - a - b + d - e - f + g);
                  for (int i = imin; i <= imax; i++) {
                    int m = sum - a - e - i;
                    int j = sum - m - g - d;
                    int n = sum - b - f - j;
                    int k2 = sum - a - c - f - g + m + n;
                    if (k2 % 2 == 0) {
                      int k = k2 / 2;
                      if (k < 0 || k > 9) continue;
                      int l = sum - i - j - k;
                      if (l < 0 || l > 9) continue;
                      int o = sum - c - g - k;
                      if (o < 0 || o > 9) continue;
                      int p = sum - m - n - o;
                      if (p < 0 || p > 9) continue;
                      cnt++;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return cnt;
  }
}
