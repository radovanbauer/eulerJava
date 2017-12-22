package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem511 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem511().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long n = 1234567898765L;
    int k = 4321;
    long mod = 1_000_000_000L;
    List<Long> divs = divisors(n);
    long[] v = new long[k];
    for (long div : divs) {
      v[LongMath.mod(div, k)] = 1;
    }
    long[] x = new long[k];
    x[0] = 1;
    long m = n;
    while (m > 0) {
      if ((m & 1) == 1) {
        x = cyclicConvolve(x, v, mod);
      }
      v = cyclicConvolve(v, v, mod);
      m >>= 1;
    }
    return x[LongMath.mod(-n, k)];
  }

  private long[] cyclicConvolve(long[] a, long[] b, long mod) {
    checkArgument(a.length == b.length);
    long[] c = new long[a.length];
    for (int i = 0; i < a.length; i++) {
      long sum = 0;
      for (int ai = 0; ai < a.length; ai++) {
        int bi = i - ai >= 0 ? i - ai : i - ai + a.length;
        sum = (sum + a[ai] * b[bi]) % mod;
      }
      c[i] = sum;
    }
    return c;
  }

  private List<Long> divisors(long n) {
    List<Long> divisors = new ArrayList<>();
    for (long d = 1; d * d <= n; d++) {
      if (n % d == 0) {
        divisors.add(d);
        if (d * d < n) {
          divisors.add(n / d);
        }
      }
    }
    Collections.sort(divisors);
    return divisors;
  }
}
