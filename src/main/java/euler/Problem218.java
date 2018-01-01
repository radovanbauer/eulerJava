package euler;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;

public class Problem218 {

  public static void main(String[] args) {
    System.out.println(new Problem218().solve(10000000000000000L));
  }

  public long solve(long maxc) {
    long cnt = 0;
    for (long k = 1; k * k * k * k <= maxc; k++) {
      for (long l = 1; (k * k + l * l) * (k * k + l * l) <= maxc; l++) {
        if ((k + l) % 2 == 0) continue;
        if (LongMath.gcd(k, l) != 1) continue;
        long m = k * k - l * l;
        long n = 2 * k * l;
        long a = m * m - n * n;
        long b = 2 * m * n;
        Preconditions.checkState(
            (a % 8 == 0 || b % 8 == 0 || (a % 4 == 0 && b % 2 == 0) || (a % 2 == 0 && b % 4 == 0))
            && (a % 3 == 0 || b % 3 == 0)
            && (a % 7 == 0 || b % 7 == 0));
        cnt++;
      }
    }
    return cnt;
  }
}
