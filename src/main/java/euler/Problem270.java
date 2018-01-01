package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;

public class Problem270 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem270().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  private static final long MOD = 100_000_000;

  public long solve() {
    int n = 30;
    List<Point> points = new ArrayList<>();
    Point lastPoint = Point.create(0, 0);
    for (int[] dir : new int[][] {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
      for (int i = 0; i < n; i++) {
        lastPoint = Point.create(lastPoint.x() + dir[0], lastPoint.y() + dir[1]);
        points.add(lastPoint);
      }
    }
    return count(Polygon.create(points));
  }

  private final Map<Polygon, Long> countCache = new HashMap<>();

  private long count(Polygon polygon) {
    int size = polygon.points().size();
    if (size == 3) {
      return 1;
    }
    if (countCache.containsKey(polygon)) {
      return countCache.get(polygon);
    }
    Point point0 = polygon.points().get(0);
    Point point1 = polygon.points().get(1);
    Vector side0 = Vector.create(point0, point1);
    long count = 0;
    for (int i = 2; i < size; i++) {
      Point point2 = polygon.points().get(i);
      Vector side1 = Vector.create(point1, point2);
      Vector side2 = Vector.create(point2, point0);
      if (side0.isParallelWith(side1)) {
        continue;
      }
      if (i > 2) {
        if (side1.isParallelWith(Vector.create(point1, polygon.points().get(2)))) {
          continue;
        }
      }
      if (i < size - 1) {
        if (side2.isParallelWith(Vector.create(polygon.points().get(size - 1), point0))) {
          continue;
        }
      }
      List<Polygon> remainingPolygons = new ArrayList<>();
      if (i > 2) {
        remainingPolygons.add(Polygon.create(polygon.points().subList(1, i + 1)));
      }
      if (i < size - 1) {
        ImmutableList<Point> points = ImmutableList.<Point>builder()
            .addAll(polygon.points().subList(i, size))
            .add(point0)
            .build();
        remainingPolygons.add(Polygon.create(points));
      }
      count += remainingPolygons.stream().mapToLong(this::count).reduce(1L, (a, b) -> a * b % MOD);
      count %= MOD;
    }
    countCache.put(polygon, count);
    return count;
  }

  @AutoValue
  static abstract class Point {
    abstract long x();
    abstract long y();

    static Point create(long x, long y) {
      return new AutoValue_Problem270_Point(x, y);
    }
  }

  @AutoValue
  static abstract class Vector {
    abstract long x();
    abstract long y();

    static Vector create(Point a, Point b) {
      return new AutoValue_Problem270_Vector(b.x() - a.x(), b.y() - a.y());
    }

    long crossProduct(Vector that) {
      return this.x() * that.y() - this.y() * that.x();
    }

    boolean isParallelWith(Vector that) {
      return crossProduct(that) == 0;
    }
  }

  @AutoValue
  static abstract class Polygon {
    abstract ImmutableList<Point> points();

    static Polygon create(List<Point> points) {
      checkArgument(points.size() >= 3);
      Point minPoint = points.get(0);
      int minIdx = 0;
      for (int idx = 1; idx < points.size(); idx++) {
        Point point = points.get(idx);
        if (point.x() < minPoint.x() || (point.x() == minPoint.x() && point.y() < minPoint.y())) {
          minPoint = point;
          minIdx = idx;
        }
      }
      ImmutableList<Point> normalizedPoints = ImmutableList.<Point>builder()
          .addAll(points.subList(minIdx, points.size()))
          .addAll(points.subList(0, minIdx))
          .build();
      return new AutoValue_Problem270_Polygon(normalizedPoints);
    }
  }
}
