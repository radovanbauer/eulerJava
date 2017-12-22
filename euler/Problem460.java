package euler;

import com.google.auto.value.AutoValue;
import com.google.common.collect.TreeMultimap;
import com.google.common.math.DoubleMath;

import javax.annotation.Nullable;
import java.math.RoundingMode;
import java.util.*;

public class Problem460 {
  public static void main(String[] args) {
    Runner.run(new Problem460()::solve);
  }

  // 5,100: 18.420738199, 30,106,146,177

  public String solve() {
    int n = 10_000;
    double rad = n / 2D;
    double spread = 0.5;
    int maxxjump = 100;

    Map<Point, Double> distance = new HashMap<>();
    Map<Point, Point> parent = new HashMap<>();
    Set<Point> visited = new HashSet<>();
    TreeMultimap<Double, PointWithDistance> queue = TreeMultimap.create();
    Map<Point, PointWithDistance> queueByPoint = new HashMap<>();
    PointWithDistance start = PointWithDistance.create(Point.create(0, 1), null, 0);
    queue.put(start.distance(), start);
    queueByPoint.put(start.point(), start);

    while (true) {
      PointWithDistance point = queue.get(queue.asMap().firstKey()).pollFirst();
      queueByPoint.remove(point.point());
      if (visited.contains(point.point())) {
        continue;
      }

      distance.put(point.point(), point.distance());
      if (point.parent() != null) {
        parent.put(point.point(), point.parent());
      }
      visited.add(point.point());

      if (point.point().x() == n && point.point().y() == 1) {
        break;
      }

      if (point.point().x() <= n) {
        int x0 = point.point().x();
        int y0 = point.point().y();
        int minx = x0;
        int maxx = Math.min(n, x0 + maxxjump);
        for (int x1 = minx; x1 <= maxx; x1++) {
          int maxy = DoubleMath.roundToInt(Math.sqrt((rad + spread) * (rad + spread) - (rad - x1) * (rad - x1)), RoundingMode.UP);
          int miny = Math.max(1, DoubleMath.roundToInt(Math.sqrt(Math.max(0, (rad - spread) * (rad - spread) - (rad - x1) * (rad - x1))), RoundingMode.DOWN));
          for (int y1 = miny; y1 <= maxy; y1++) {
            Point newPoint = Point.create(x1, y1);
            if (visited.contains(newPoint)) {
              continue;
            }
            double s = calculateDistance(x0, y0, x1, y1);
            PointWithDistance newPointWithDistance = PointWithDistance.create(newPoint, point.point(), point.distance() + s);

            @Nullable PointWithDistance prevPointWithDistance = queueByPoint.get(newPoint);
            if (prevPointWithDistance != null && prevPointWithDistance.distance() > newPointWithDistance.distance()) {
              queue.get(prevPointWithDistance.distance()).remove(prevPointWithDistance);
              queueByPoint.remove(newPoint);
              queue.put(newPointWithDistance.distance(), newPointWithDistance);
              queueByPoint.put(newPoint, newPointWithDistance);
            } else if (prevPointWithDistance == null) {
              queue.put(newPointWithDistance.distance(), newPointWithDistance);
              queueByPoint.put(newPoint, newPointWithDistance);
            }
          }
        }
      }
    }

    List<Point> path = new ArrayList<>();
    Point point = Point.create(n, 1);
    while (point != null) {
      path.add(point);
      point = parent.get(point);
    }
    Collections.reverse(path);

    System.out.println(path);

    return String.format("%.9f", distance.get(Point.create(n, 1)));
  }

  private double calculateDistance(int x0, int y0, int x1, int y1) {
    double v;
    if (y0 == y1) {
      v = y0;
    } else {
      v = (y1 - y0) / (Math.log(y1) - Math.log(y0));
    }
    return Math.sqrt((x1 - x0)*(x1 - x0) + (y1 - y0) * (y1 - y0)) / v;
  }

  @AutoValue
  static abstract class Point {
    abstract int x();
    abstract int y();

    static Point create(int x, int y) {
      return new AutoValue_Problem460_Point(x, y);
    }
  }

  @AutoValue
  static abstract class PointWithDistance implements Comparable<PointWithDistance> {
    abstract Point point();
    abstract @Nullable Point parent();
    abstract double distance();

    static PointWithDistance create(Point point, @Nullable Point parent, double distance) {
      return new AutoValue_Problem460_PointWithDistance(point, parent, distance);
    }

    @Override
    public int compareTo(PointWithDistance that) {
      return Double.compare(this.distance(), that.distance());
    }
  }
}
