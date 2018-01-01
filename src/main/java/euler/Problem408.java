package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Problem408 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem408().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  private static final int MOD = 1_000_000_007;

  public long solve() {
    int max = 10_000_000;
    List<Point> points = new ArrayList<>();
    for (long m = 2; 4 * m * m <= max; m++) {
      for (long n = 1; 4 * m * m * n * n <= max; n++) {
        if ((m - n) % 2 == 1 && LongMath.gcd(m, n) == 1) {
          long k = 1;
          while (k * k * (m * m - n * n) * (m * m - n * n) <= max && k * k * 4 * m * m * n * n <= max) {
            int a = Ints.checkedCast(k * k * (m * m - n * n) * (m * m - n * n));
            int b = Ints.checkedCast(k * k * 4 * m * m * n * n);
            points.add(Point.create(a, b));
            points.add(Point.create(b, a));
            k++;
          }
        }
      }
    }
    points.add(Point.create(max, max));
    Collections.sort(points, Comparator.comparing(Point::x).thenComparing(Point::y));
    LongMod[] admissible = new LongMod[points.size()];
    for (int i = 0; i < points.size(); i++) {
      Point point = points.get(i);
      LongMod count = allPathsBetween(Point.create(0, 0), point);
      for (int j = 0; j < i; j++) {
        Point firstInadmissible = points.get(j);
        if (point.x() >= firstInadmissible.x() && point.y() >= firstInadmissible.y()) {
          count = count.subtract(admissible[j].multiply(allPathsBetween(firstInadmissible, point)));
        }
      }
      admissible[i] = count;
    }
    return admissible[admissible.length - 1].n();
  }

  private LongMod allPathsBetween(Point from, Point to) {
    checkArgument(from.x() <= to.x() && from.y() <= to.y());
    return binomialMod(to.x() - from .x() + to.y() - from.y(), to.x() - from.x());
  }

  private long[] factorial = new long[20_000_002];
  private long[] factorialInverse = new long[20_000_002];
  {
    factorial[0] = 1;
    for (int i = 1; i < factorial.length; i++) {
      factorial[i] = LongMod.create(factorial[i - 1], MOD).multiply(i).n();
    }
    for (int i = 0; i < factorial.length; i++) {
      factorialInverse[i] = LongMod.create(factorial[i], MOD).invert().n();
    }
  }

  private LongMod binomialMod(int n, int k) {
    return LongMod.create(factorial[n], MOD)
        .multiply(factorialInverse[k]).multiply(factorialInverse[n - k]);
  }

  @AutoValue
  static abstract class Point {
    abstract int x();
    abstract int y();

    static Point create(int x, int y) {
      return new AutoValue_Problem408_Point(x, y);
    }
  }
}
