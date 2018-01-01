package euler;

import com.google.auto.value.AutoValue;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.math.LongMath;

import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

import static euler.Runner.run;

public class Problem583 {

  public static void main(String[] args) {
    run(new Problem583()::solve);
  }

  public long solve() {
    long max = 10_000_000;
    Multimap<Long, Long> triplesBySide = ArrayListMultimap.create();
    Set<Double> doubles = new HashSet<>();
    for (long m = 2; m * m <= max; m++) {
      for (long n = 1; n < m && m * m + n * n <= max; n++) {
        if ((m & 1) != (n & 1) && LongMath.gcd(m, n) == 1) {
          long aBase = m * m - n * n;
          long bBase = 2 * m * n;
          long cBase = m * m + n * n;
          for (long k = 1; k * cBase <= max; k++) {
            long a = k * aBase;
            long b = k * bBase;
            long c = k * cBase;
            if (a > b) {
              long tmp = a;
              a = b;
              b = tmp;
            }
            triplesBySide.put(a, b);
            triplesBySide.put(b, a);
            doubles.add(Double.create(a, b));
          }
        }
      }
    }

    long sum = 0;
    for (long ah : triplesBySide.keySet()) {
      long a = ah * 2;
      for (long x : triplesBySide.get(ah)) {
        long c = LongMath.sqrt(ah * ah + x * x, RoundingMode.UNNECESSARY);
        for (long y : triplesBySide.get(ah)) {
          if (y > x && y > 2 * x) {
            long b = y - x;
            long p = a + 2 * b + 2 * c;
            if (p <= max && doubles.contains(Double.create(Math.min(a, b), Math.max(a, b)))) {
              sum += p;
            }
          }
        }
      }

    }

    return sum;
  }

  @AutoValue
  static abstract class Triple {
    abstract long a();
    abstract long b();
    abstract long c();

    static Triple create(long a, long b, long c) {
      Preconditions.checkArgument(a < b && b < c);
      return new AutoValue_Problem583_Triple(a, b, c);
    }

    long other(long x) {
      if (a() == x) {
        return b();
      } else {
        return a();
      }
    }
  }

  @AutoValue
  static abstract class Double {
    abstract long a();
    abstract long b();

    static Double create(long a, long b) {
      Preconditions.checkArgument(a < b);
      return new AutoValue_Problem583_Double(a, b);
    }
  }
}
