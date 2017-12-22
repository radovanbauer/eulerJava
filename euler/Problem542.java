package euler;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem542 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem542().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  private long solve() {
    long n = LongMath.pow(10, 17);
    TreeMap<Long, Long> xys = new TreeMap<>();
    for (long d = 2; d <= 50; d++) {
      long x = d;
      long z = d - 1;
      while (x <= n / d) {
        x = LongMath.checkedMultiply(x, d);
        z = LongMath.checkedMultiply(z, d - 1);
        long y = BigInteger.valueOf(x).multiply(BigInteger.valueOf(d))
            .subtract(BigInteger.valueOf(z).multiply(BigInteger.valueOf(d - 1))).longValueExact();
        Map.Entry<Long, Long> floorEntry = xys.floorEntry(x);
        if (floorEntry == null || floorEntry.getValue() < y) {
          xys.put(x, y);
          Map.Entry<Long, Long> higherEntry = xys.higherEntry(x);
          while (higherEntry != null && higherEntry.getValue() <= y) {
            xys.remove(higherEntry.getKey());
            higherEntry = xys.higherEntry(x);
          }
        }
      }
    }
    if (n % 2 == 0) {
      return s(4, xys) + t(5, n, xys);
    } else {
      return s(4, xys) + t(5, n - 1, xys) + s(n, xys);
    }
  }

  private long s(long k, TreeMap<Long, Long> xys) {
    long res = 0;
    for (Map.Entry<Long, Long> xy : xys.entrySet()) {
      long x = xy.getKey();
      long y = xy.getValue();
      if (x > k) {
        break;
      }
      res = Math.max(res, k / x * y);
    }
    return res;
  }

  private long t(long a, long b, TreeMap<Long, Long> xys) {
    if (a + 1 == b) {
      if (b % 2 == 0) {
        return s(b, xys) - s(a, xys);
      } else {
        return 0;
      }
    } else {
      long c = (a + b) / 2;
      long sa = s(a, xys);
      long sb = s(b, xys);
      long sc = s(c, xys);
      long res = 0;
      if (sa != sc) {
        res += t(a, c, xys);
      }
      if (sc != sb) {
        res += t(c, b, xys);
      }
      return res;
    }
  }
}
