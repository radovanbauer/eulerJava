package euler;

import static com.google.common.math.DoubleMath.fuzzyCompare;
import static com.google.common.math.IntMath.gcd;
import static java.lang.Math.abs;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;

public class Problem184 {

  public static void main(String[] args) {
    System.out.println(new Problem184().solve(105));
  }

  public long solve(int r) {
    long cnt = 0L;
    List<Point> pointsList = Lists.newArrayList();
    for (int x = -r + 1; x < r; x++) {
      for (int y = -r + 1; y < r; y++) {
        if (x * x + y * y < r * r && !(x == 0 && y == 0)) {
          pointsList.add(Point.create(x, y));
        }
      }
    }
    Points points = new Points(pointsList);
    Map<Double, Integer> angleCounts = Maps.newHashMap();
    for (Point a : points) {
      if (!angleCounts.containsKey(a.negAngle)) {
        angleCounts.put(a.negAngle, 1);
      } else {
        angleCounts.put(a.negAngle, angleCounts.get(a.negAngle) + 1);
      }
    }
    for (Double a : angleCounts.keySet()) {
      for (Double b : angleCounts.keySet()) {
        double angle = b - a;
        if (angle < 0) {
          angle += 2 * Math.PI;
        }
        if (fuzzyCompare(angle, Math.PI, 1e-10) < 0) {
          cnt += points.pointCountBetween(a, b) * angleCounts.get(a) * angleCounts.get(b);
        }
      }
    }
    return cnt / 3L;
  }

  private static class Points implements Iterable<Point> {
    private final ImmutableList<Point> points;
    private final ImmutableSortedMap<Double, Integer> pointCounts;
    private final int totalCnt;

    public Points(Iterable<Point> points) {
      this.points = ImmutableList.copyOf(points);
      HashMultiset<Double> counts = HashMultiset.create();
      for (Point point : points) {
        counts.add(point.angle);
      }
      List<Double> sortedAngles = Ordering.natural().sortedCopy(counts.elementSet());
      ImmutableSortedMap.Builder<Double, Integer> pointCountsBuilder =
          ImmutableSortedMap.naturalOrder();
      int cnt = 0;
      pointCountsBuilder.put(-Math.PI, 0);
      for (double angle : sortedAngles) {
        cnt += counts.count(angle);
        pointCountsBuilder.put(angle, cnt);
      }
      pointCountsBuilder.put(Math.PI + 1e-10, cnt);
      this.pointCounts = pointCountsBuilder.build();
      this.totalCnt = cnt;
    }

    public int pointCountBetween(double a, double b) {
      int cmp = Doubles.compare(a, b);
      if (cmp == 0) {
        return 0;
      } else if (cmp < 0) {
        return pointCounts.lowerEntry(b).getValue()
            - pointCounts.floorEntry(a).getValue();
      } else {
        return totalCnt
            - pointCounts.ceilingEntry(a).getValue()
            + pointCounts.lowerEntry(b).getValue();
      }
    }

    @Override
    public Iterator<Point> iterator() {
      return points.iterator();
    }
  }

  private static class Point {
    private final int x, y;
    private final double angle, negAngle;

    public static Point create(int x, int y) {
      int g = gcd(abs(x), abs(y));
      return new Point(x, y, Math.atan2(y / g, x / g), Math.atan2(-y / g, -x / g));
    }

    private Point(int x, int y, double angle, double negAngle) {
      this.x = x;
      this.y = y;
      this.angle = angle;
      this.negAngle = negAngle;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(x, y);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Point) {
        Point that = (Point) obj;
        return this.x == that.x
            && this.y == that.y;
      }
      return false;
    }

    @Override
    public String toString() {
      return String.format("[%d,%d:%.2f]", x, y, angle);
    }
  }
}
