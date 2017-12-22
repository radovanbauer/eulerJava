package euler;

import static com.google.common.math.LongMath.gcd;

import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem504 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem504().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long count = 0;
    long m = 100;
    for (long a = 1; a <= m; a++) {
      for (long b = 1; b <= m; b++) {
        for (long c = 1; c <= m; c++) {
          for (long d = 1; d <= m; d++) {
            long area = (a * b + b * c + c * d + d * a
                - (gcd(a, b) + gcd(b, c) + gcd(c, d) + gcd(d, a)))
                / 2 + 1;
            if (isSquare(area)) {
              count++;
            }
          }
        }
      }
    }
    return count;
  }

  private boolean isSquare(long n) {
    long sqrt = LongMath.sqrt(n, RoundingMode.FLOOR);
    return sqrt * sqrt == n;
  }
}
