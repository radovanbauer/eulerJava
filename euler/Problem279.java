package euler;

import static com.google.common.base.Preconditions.checkState;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem279 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem279().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int max = 100_000_000;
    return countAllTriples(primitiveTriples90(max), max)
        + countAllTriples(primitiveTriples60(max), max)
        + countAllTriples(primitiveTriples120(max), max);
  }

  private long countAllTriples(Iterable<Triple> primitiveTriples, int max) {
    long count = 0;
    for (Triple triple : primitiveTriples) {
      long p = triple.a() + triple.b() + triple.c();
      count += max / p;
    }
    return count;
  }

  private Set<Triple> primitiveTriples90(int max) {
    Set<Triple> triples = new HashSet<>();
    for (long n = 1; 2 * (n + 1) * (n + 1) + 2 * (n + 1) * n <= max; n++) {
      for (long m = n + 1; 2 * m * m + 2 * m * n <= max; m++) {
        if ((m - n) % 2 != 0 && LongMath.gcd(m, n) == 1) {
          long a = m * m - n * n;
          long b = 2 * m * n;
          long c = m * m + n * n;
          if (a <= b) {
            triples.add(Triple.create(a, b, c));
          } else {
            triples.add(Triple.create(b, a, c));
          }
        }
      }
    }
    return triples;
  }

  private Set<Triple> primitiveTriples60(int max) {
    Set<Triple> triples = new HashSet<>();
    if (max >= 1) {
      triples.add(Triple.create(1, 1, 1));
    }
    for (long n = 1; 2 * (n + 1) * (n + 1) + 5 * (n + 1) * n + 2 * n * n <= max; n++) {
      for (long m = n + 1; 2 * m * m + 5 * m * n + 2 * n * n <= max; m++) {
        if ((m - n) % 3 != 0 && LongMath.gcd(m, n) == 1) {
          long a = n * n + 2 * m * n;
          long b = m * m + 2 * m * n;
          long c = m * m + m * n + n * n;
          checkState(a < b);
          triples.add(Triple.create(a, b, c));
        }
      }
    }
    for (long n = 1; (2 * (n + 1) * (n + 1) + 5 * (n + 1) * n + 2 * n * n) / 3 <= max; n++) {
      for (long m = n + 1; (2 * m * m + 5 * m * n + 2 * n * n) / 3 <= max; m++) {
        if ((m - n) % 3 == 0 && LongMath.gcd(m, n) == 1) {
          long a = (n * n + 2 * m * n) / 3;
          long b = (m * m + 2 * m * n) / 3;
          long c = (m * m + m * n + n * n) / 3;
          checkState(a < b);
          triples.add(Triple.create(a, b, c));
        }
      }
    }
    return triples;
  }

  private Set<Triple> primitiveTriples120(int max) {
    Set<Triple> triples = new HashSet<>();
    for (long n = 1; 2 * (n + 1) * (n + 1) + 3 * (n + 1) * n + n * n <= max; n++) {
      for (long m = n + 1; 2 * m * m + 3 * m * n + n * n <= max; m++) {
        if ((m - n) % 3 != 0 && LongMath.gcd(m, n) == 1) {
          long a = m * m - n * n;
          long b = 2 * m * n + n * n;
          long c = m * m + m * n + n * n;
          if (a < b) {
            triples.add(Triple.create(a, b, c));
          } else {
            triples.add(Triple.create(b, a, c));
          }
        }
      }
    }
    return triples;
  }

  @AutoValue
  static abstract class Triple {
    abstract long a();
    abstract long b();
    abstract long c();

    static Triple create(long a, long b, long c) {
      return new AutoValue_Problem279_Triple(a, b, c);
    }
  }
}
