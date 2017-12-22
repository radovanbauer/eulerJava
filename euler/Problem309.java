package euler;

import static com.google.common.math.LongMath.checkedMultiply;
import static java.math.RoundingMode.UNNECESSARY;

import java.util.ArrayList;
import java.util.List;

import com.google.common.math.IntMath;
import com.google.common.math.LongMath;

public class Problem309 {

  public static void main(String[] args) {
    System.out.println(new Problem309().solve());
  }

  public long solve() {
    int max = 1_000_000;
    FactorizationSieve sieve = new FactorizationSieve(max);
    long count = 0;
    for (int w = 1; w < max; w++) {
      List<Long> ladders = new ArrayList<>();
      for (int div : sieve.divisors(w)) {
        int wp = w / div;
        if (wp % 2 == 0) {
          for (int m : sieve.divisors(wp / 2)) {
            int n = wp / (2 * m);
            if (m > n && n > 0 && (m - n) % 2 == 1 && IntMath.gcd(m, n) == 1) {
              long x = div * (1L * m * m + 1L * n * n);
              if (x < max) {
                ladders.add(x);
              }
            }
          }
        } else {
          for (int d1 : sieve.divisors(wp)) {
            int d2 = wp / d1;
            int m = (d1 + d2) / 2;
            int n = (d2 - d1) / 2;
            if (m > n && n > 0 && (m - n) % 2 == 1 && IntMath.gcd(m, n) == 1) {
              long x = div * (1L * m * m + 1L * n * n);
              if (x < max) {
                ladders.add(x);
              }
            }
          }
        }
      }
      for (long x : ladders) {
        for (long y : ladders) {
          if (x < y) {
            long s1 = LongMath.sqrt(x * x - 1L * w * w, UNNECESSARY);
            long s2 = LongMath.sqrt(y * y - 1L * w * w, UNNECESSARY);
            if (checkedMultiply(s1, s2) % (s1 + s2) == 0) {
              count++;
            }
          }
        }
      }
    }
    return count;
  }
}
