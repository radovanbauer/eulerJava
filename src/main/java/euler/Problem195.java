package euler;

import com.google.common.math.LongMath;

public class Problem195 {

  public static void main(String[] args) {
    System.out.println(new Problem195().solve(1053779));
  }

  public long solve(int max) {
    long cnt = 0L;
    for (long n = 1; n * n * n * n < 12L * max * max; n++) {
      for (long m = 2 * n + 1; (m - n) * (m - n) * n * n <= 12L * max * max; m++) {
        if (LongMath.gcd(m, n) != 1) continue;
        if ((m + n) % 3 == 0) {
          cnt += (int) (2D * Math.sqrt(3) * max / ((m - n) * n));
        } else {
          cnt += (int) (2D / 3D * Math.sqrt(3) * max / ((m - n) * n));
        }
      }
    }
    return cnt;
  }
}
