package euler;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

import java.math.RoundingMode;
import java.util.*;

public class Problem576 {
  public static void main(String[] args) {
    Runner.run(new Problem576()::solve);
  }

  public String solve() {
    int n = 100;
    double g = 2D / 100_000;
    ImmutableList<Long> primes = ImmutableList.copyOf(PrimeSieve.create(n));
    Set<Double> allPoints = new HashSet<>();
    for (long p : primes) {
      allPoints.addAll(getPoints(Ints.checkedCast(p), g).keySet());
      System.out.println(p + ": " + allPoints.size());
    }
    Map<Double, Double> sum = new HashMap<>();
    allPoints.forEach(d -> sum.put(d, 0D));
    for (long p : primes) {
      TreeMap<Double, Point> points = getPoints(Ints.checkedCast(p), g);
      for (double d : allPoints) {
        double gapEnd = d + g;
        if (gapEnd >= 1) {
          continue;
        }
        double minDist = Double.MAX_VALUE;
        for (Map.Entry<Double, Point> pointEntry : points.tailMap(d, false).entrySet()) {
          if (pointEntry.getKey() > gapEnd) {
            break;
          }
          minDist = Math.min(minDist, pointEntry.getValue().f() + pointEntry.getKey());
        }
        sum.put(d, sum.get(d) + minDist);
      }
    }
    return String.format("%.4f", Collections.max(sum.values()));
  }

  private TreeMap<Double, Point> getPoints(int p, double g) {
    TreeMap<Double, Point> points = new TreeMap<>();
    points.put(0D, Point.create(0, 0));
    points.put(1D, Point.create(0, -1));
    int large = 1;
    for (long j = 1;; j++) {
      double totalPos = Math.sqrt((double) j * j / p);
      long f = LongMath.sqrt(j * j / p, RoundingMode.FLOOR);
      double pos = totalPos - f;
      double prev = points.floorEntry(pos).getKey();
      double next = points.ceilingEntry(pos).getKey();
      if (next - prev >= g) {
        large--;
      }
      Point point = Point.create(j, f);
      points.put(pos, point);
      if (pos - prev >= g) {
        large++;
      }
      if (next - pos >= g) {
        large++;
      }

      if (large == 0) {
        return points;
      }
    }
  }

  @AutoValue
  static abstract class Point {
    abstract long j();
    abstract long f();
    static Point create(long j, long f) {
      return new AutoValue_Problem576_Point(j, f);
    }
  }
}
