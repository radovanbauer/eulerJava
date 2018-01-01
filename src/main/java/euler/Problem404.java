package euler;

import com.google.common.math.LongMath;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.math.LongMath.gcd;

public class Problem404 {
  public static void main(String[] args) {
    Runner.run(new Problem404()::solve);
  }

  public long solve() {
    long maxa = LongMath.pow(10, 17);

    long count = 0;
    for (long m = 1; LongMath.checkedPow(m, 4) <= maxa; m++) {
      for (long n = m % 2 + 1; n < m; n += 2) {
        if (LongMath.gcd(m, n) != 1) {
          continue;
        }
        {
          long x = Math.abs(m * m - n * n - m * n);
          long z = 4 * m * n + m * m - n * n;
          long y = m * m + n * n;
          checkState(x > 0 && z > 0 && y > 0);
          if (x < y && y < 2 * x && y < z && z < 2 * y && x * z <= maxa
              && gcd(x, y) == 1 && gcd(x, z) == 1 && gcd(y, z) == 1) {
            count += maxa / (x * z);
          }
        }
        {
          long x = m * m - n * n + m * n;
          long z = Math.abs(4 * m * n - m * m + n * n);
          long y = m * m + n * n;
          checkState(x > 0 && z > 0 && y > 0);
          if (x < y && y < 2 * x && y < z && z < 2 * y && x * z <= maxa
              && gcd(x, y) == 1 && gcd(x, z) == 1 && gcd(y, z) == 1) {
            count += maxa / (x * z);
          }
        }
      }
    }
    return count;
  }
}
