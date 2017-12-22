package euler;

import java.math.RoundingMode;

import com.google.common.math.LongMath;

public class Problem372 {

  public static void main(String[] args) {
    System.out.println(new Problem372().solve());
  }

  public long solve() {
    long m = 2 * 1_000_000;
    long n = 1_000_000_000;
    long minr = (m + 1) * (m + 1) / (n * n);
    if (minr % 2 == 0) {
      minr++;
    }
    long maxr = n * n / ((m + 1) * (m + 1));
    if (maxr % 2 == 0) {
      maxr--;
    }
    if (minr > maxr) {
      return 0;
    }
    System.out.println(minr + " - " + maxr);
    long sum = 0;
    for (long r = minr; r <= maxr; r += 2) {
      System.out.println(r);
      for (long y = m + 1; y <= n; y++) {
        long minx = LongMath.sqrt(r * y * y, RoundingMode.CEILING);
        if (minx > n) {
          break;
        }
        long sqrt = LongMath.sqrt((r + 1) * y * y, RoundingMode.FLOOR);
        long maxx;
        if (sqrt * sqrt == (r + 1) * y * y) {
          maxx = Math.min(sqrt - 1, n);
        } else {
          maxx = Math.min(sqrt, n);
        }
        sum += maxx - minx + 1;
      }
    }
    return sum;
  }
}
