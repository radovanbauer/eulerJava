package euler;

import java.util.ArrayDeque;

import com.google.auto.value.AutoValue;

public class Problem395 {

  public static void main(String[] args) {
    System.out.println(String.format("%.10f", new Problem395().solve()));
  }

  public double solve() {
    Result result = new Result();
    ArrayDeque<Square> queue = new ArrayDeque<>();
    queue.addLast(Square.create(1, Point.create(0, 0), Point.create(1, 0)));
    int curLevel = 0;
    double prevArea = -1D;
    while (true) {
      Square square = queue.removeFirst();
      if (square.level() > curLevel) {
        curLevel = square.level();
        if (Math.abs(result.area() - prevArea) < 1e-15) {
          break;
        }
        prevArea = result.area();
      }
      Point b1 = square.b1();
      Point b2 = square.b2();
      double dx = b2.x() - b1.x();
      double dy = b2.y() - b1.y();
      Point m1 = Point.create(b1.x() + dx * minx - dy * miny, b1.y() + dy * minx + dx * miny);
      Point m2 = Point.create(b1.x() + dx * maxx - dy * miny, b1.y() + dy * maxx + dx * miny);
      Point m3 = Point.create(b1.x() + dx * maxx - dy * maxy, b1.y() + dy * maxx + dx * maxy);
      Point m4 = Point.create(b1.x() + dx * minx - dy * maxy, b1.y() + dy * minx + dx * maxy);
      if (!result.containsAll(m1, m2, m3, m4)) {
        Point b3 = Point.create(b2.x() - dy, b2.y() + dx);
        Point b4 = Point.create(b3.x() - dx, b3.y() - dy);
        Point b5 = Point.create(
            b4.x() + 4D/5D * (dx * 4D/5D - dy * 3D/5D),
            b4.y() + 4D/5D * (dx * 3D/5D + dy * 4D/5D));
        result.add(b1, b2, b3, b4);
        queue.addLast(Square.create(square.level() + 1, b4, b5));
        queue.addLast(Square.create(square.level() + 1, b5, b3));
      }
    }
    return result.area();
  }

  @AutoValue
  static abstract class Square {
    abstract int level();
    abstract Point b1();
    abstract Point b2();
    static Square create(int level, Point b1, Point b2) {
      return new AutoValue_Problem395_Square(level, b1, b2);
    }
  }

  private static final double minx = -3.25D, maxx = 3.14D, miny = -0.12D, maxy = 4.33D;

  @AutoValue
  static abstract class Point {
    abstract double x();
    abstract double y();

    static Point create(double x, double y) {
      return new AutoValue_Problem395_Point(x, y);
    }

    Point add(Point that) {
      return create(this.x() + that.x(), this.y() + that.y());
    }

    Point multiply(double d) {
      return create(this.x() * d, this.y() * d);
    }

    double distance(Point that) {
      double dx = this.x() - that.x();
      double dy = this.y() - that.y();
      return Math.sqrt(dx * dx + dy * dy);
    }
  }

  static class Result {
    double xmin = 0;
    double xmax = 0;
    double ymin = 0;
    double ymax = 0;

    void add(Point... points) {
      for (Point point : points) {
        xmin = Math.min(xmin, point.x());
        xmax = Math.max(xmax, point.x());
        ymin = Math.min(ymin, point.y());
        ymax = Math.max(ymax, point.y());
      }
    }

    boolean containsAll(Point... points) {
      for (Point point : points) {
        if (!contains(point)) {
          return false;
        }
      }
      return true;
    }

    boolean contains(Point point) {
      return point.x() >= xmin && point.x() <= xmax && point.y() >= ymin && point.y() <= ymax;
    }

    double area() {
      return (xmax - xmin) * (ymax - ymin);
    }

    @Override
    public String toString() {
      return xmin + " " + xmax + " " + ymin + " " + ymax;
    }
  }
}
