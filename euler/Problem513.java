package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.auto.value.AutoValue;
import com.google.common.base.MoreObjects;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;


public class Problem513 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem513().solve(stopwatch));
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve(Stopwatch stopwatch) {
    long n = 100_000;
    long segment = 10_000;
//    for (long c = 2; c <= n; c += 2) {
//      for (long a = 1; a <= c; a++) {
//        long minb = Math.max(c - a + 1, a);
//        if ((minb - a) % 2 != 0) {
//          minb++;
//        }
//        for (long b = minb; b <= c; b += 2) {
//          long r = 2 * a * a + 2 * b * b - c * c;
//          if (r % 4 == 0) {
//            long m = LongMath.sqrt(r / 4, RoundingMode.FLOOR);
//            if (m * m * 4 == r) {
//              System.out.println(a + " " + b + " " + c + ": " + m);
//            }
//          }
//        }
//      }
//    }
//    System.out.println();
    List<Long> mins = new ArrayList<>();
    for (long min = 1; min <= n * n; min += segment) {
      mins.add(min);
    }
    AtomicInteger counter = new AtomicInteger();
    return mins.stream().parallel().mapToLong(min -> {
      int val = counter.incrementAndGet();
      if ((val & 0xFFF) == 0) {
        System.out.println(val + "/" + mins.size() + " " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
      }
      long count = 0;
      long max = Math.min(min + segment - 1, n * n);
      Sums c1mSums = Sums.create(min, max);
      Sums abSums = Sums.create(min * 2, max * 2);
      for (long sum = min; sum <= max; sum++) {
        count += count(n, abSums.get(sum * 2), c1mSums.get(sum));
      }
      return count;
    }).sum();
  }

  private long count(long n, Collection<Pair> abPairs, Collection<Pair> c1mPairs) {
    Set<Triple> triples = new HashSet<>();
    for (Pair ab : abPairs) {
      long a = ab.x();
      long b = ab.y();
      checkArgument(a <= b);
      for (Pair c1m : c1mPairs) {
        long c = c1m.x() * 2;
        if (b <= c && a + b > c && c <= n) {
          triples.add(Triple.create(a, b, c));
        }
        c = c1m.y() * 2;
        if (b <= c && a + b > c && c <= n) {
          triples.add(Triple.create(a, b, c));
        }
      }
    }
    return triples.size();
  }

  private static class Sums {
    private final long min, max;
    private final List<Pair>[] sums;

    private Sums(long min, long max, List<Pair>[] sums) {
      this.min = min;
      this.max = max;
      this.sums = sums;
    }
  
    public static Sums create(long min, long max) {
      int size = Ints.checkedCast(max - min + 1);
      List<Pair>[] sums = new List[size];
      for (int x = 1; 1L * x * x <= max; x++) {
        int miny = Math.max(x,
            Ints.checkedCast(LongMath.sqrt(Math.max(0, min - x * x), RoundingMode.CEILING)));
        for (int y = miny;; y++) {
          long sum = 1L * x * x + 1L * y * y;
          if (sum > max) {
            break;
          }
          int idx = Ints.checkedCast(sum - min);
          if (sums[idx] == null) {
            sums[idx] = new ArrayList<>();
          }
          sums[idx].add(Pair.create(x, y));
        }
      }
      return new Sums(min, max, sums);
    }

    public Collection<Pair> get(long n) {
      checkArgument(n >= min && n <= max);
      return MoreObjects.firstNonNull(sums[Ints.checkedCast(n - min)], ImmutableList.<Pair>of());
    }
  }

  @AutoValue
  static abstract class Pair {
    abstract long x();
    abstract long y();
    static Pair create(long x, long y) {
      return new AutoValue_Problem513_Pair(x, y);
    }
  }

  @AutoValue
  static abstract class Triple {
    abstract long x();
    abstract long y();
    abstract long z();
    static Triple create(long x, long y, long z) {
      return new AutoValue_Problem513_Triple(x, y, z);
    }
  }
}
