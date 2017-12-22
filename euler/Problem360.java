package euler;

import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ComparisonChain;

public class Problem360 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem360().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long N = 10_000_000_000L;
    long pow2 = 1;
    while (N % 2 == 0) {
      pow2 *= 2;
      N /= 2;
    }
    Map<Long, Long> squares = new HashMap<>();
    for (long i = 1; i * i <= N; i++) {
      squares.put(i * i, i);
    }
    Set<Quadruple> quadruples = new TreeSet<>();
    quadruples.add(Quadruple.create(0, 0, N, N));
    for (long div : divisors(N)) {
      long k = N / div;
      for (long m = 0; m * m <= div; m++) {
        if (squares.containsKey(div - m * m)) {
          Long n = squares.get(div - m * m);
          if (m > n) {
            quadruples.add(Quadruple.create(k * (m * m - n * n), k * 2 * m * n, 0, N));
          }
        }
      }
      for (long m = 0; m * m <= div; m++) {
        for (long n = m; m * m + n * n <= div; n++) {
          if (m * m + n * n <= div / 2) {
            continue;
          }
          for (long p = 0; m * m + n * n + p * p <= div; p++) {
            if (squares.containsKey(div - m * m - n * n - p * p)) {
              long q = squares.get(div - m * m - n * n - p * p);
              if ((m + n + p + q) % 2 != 1) {
                continue;
              }
              if (m * q + n * p == 0 || n * q - m * p == 0) {
                continue;
              }
              quadruples.add(Quadruple.create(
                  k * (m * m + n * n - p * p - q * q),
                  k * 2 * (m * q + n * p),
                  k * Math.abs(2 * (n * q - m * p)),
                  N));
            }
          }
        }
      }
    }
    long sum = 0;
    for (Quadruple quad : quadruples) {
      long multiplier;
      if (quad.b() == 0) {
        multiplier = 6;
      } else if (quad.a() == 0) {
        checkState(quad.b() < quad.c());
        multiplier = 24;
      } else if (quad.a() == quad.b() || quad.b() == quad.c()) {
        multiplier = 24;
      } else {
        multiplier = 48;
      }
      sum += multiplier * pow2 * (quad.a() + quad.b() + quad.c());
    }
    return sum;
  }

  @AutoValue
  static abstract class Quadruple implements Comparable<Quadruple> {
    abstract long a();
    abstract long b();
    abstract long c();
    abstract long d();

    static Quadruple create(long a, long b, long c, long d) {
      long[] x = new long[] { a, b, c, d };
      Arrays.sort(x);
      return new AutoValue_Problem360_Quadruple(x[0], x[1], x[2], x[3]);
    }

    @Override
    public int compareTo(Quadruple that) {
      return ComparisonChain.start()
          .compare(this.a(), that.a())
          .compare(this.b(), that.b())
          .compare(this.c(), that.c())
          .result();
    }
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
