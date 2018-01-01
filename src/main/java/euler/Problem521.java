package euler;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem521 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem521().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long n = 1_000_000_000_000L;
    long mod = 1_000_000_000;
    long nsqrt = LongMath.sqrt(n, RoundingMode.FLOOR);
    Map<Long, Long> primeCount = new HashMap<>();
    Map<Long, LongMod> primeSum = new HashMap<>();
    List<Long> keys = new ArrayList<>();
    for (long i = 1; i <= nsqrt + 1; i++) {
      keys.add(n / i);
    }
    for (long i = n / (nsqrt + 1) - 1; i >= 1; i--) {
      keys.add(i);
    }
    for (long key : keys) {
      primeCount.put(key, key - 1);
      primeSum.put(key, triangleNumberMod(key, mod).subtract(1));
    }
    LongMod res = triangleNumberMod(n, mod).subtract(1);
    for (long i = 2; i <= nsqrt; i++) {
      if (primeCount.get(i) > primeCount.get(i - 1)) {
        res = res.subtract(primeSum.get(n / i).subtract(primeSum.get(i - 1)).multiply(i));
        res = res.add((primeCount.get(n / i) - primeCount.get(i - 1)) * i);
        for (long key : keys) {
          if (key < i * i) {
            break;
          }
          primeCount.put(key, primeCount.get(key) - (primeCount.get(key / i) - primeCount.get(i - 1)));
          primeSum.put(key,
              primeSum.get(key).subtract(primeSum.get(key / i).subtract(primeSum.get(i - 1)).multiply(i)));
        }
      }
    }
    return res.n();
  }

  private LongMod triangleNumberMod(long n, long mod) {
    return n % 2 == 0
        ? LongMod.create(n / 2, mod).multiply(n + 1)
        : LongMod.create(n, mod).multiply((n + 1) / 2);
  }
}
