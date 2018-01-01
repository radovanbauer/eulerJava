package euler;

import com.google.common.math.LongMath;

public class Problem296 {
  public static void main(String[] args) {
    Runner.run(new Problem296()::solve);
  }

  // r = a * c / (a + b)

  public long solve() {
    long max = 100_000;
    long count = 0;
    for (long a = 1; 3 * a <= max; a++) {
      for (long b = a; a + 2 * b <= max; b++) {
        long g = LongMath.gcd(a + b, a);
        long y = (a + b) / g;
        long minc = b;
        long maxc = Math.min(max - a - b, a + b - 1);
        count += maxc / y - (minc - 1) / y;
      }
    }
    return count;
  }
}
