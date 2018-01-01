package euler;

import static java.lang.Long.signum;
import static java.lang.Math.abs;

import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Problem165 {

  public static void main(String[] args) {
    System.out.println(new Problem165().solve(5000));
  }

  public long solve(int n) {
    List<Segment> segments = Lists.newArrayList();
    long s = 290797;
    for (int i = 1; i <= n; i++) {
      s = (s * s) % 50515093;
      long t1 = s % 500;
      s = (s * s) % 50515093;
      long t2 = s % 500;
      s = (s * s) % 50515093;
      long t3 = s % 500;
      s = (s * s) % 50515093;
      long t4 = s % 500;
      segments.add(new Segment(new Point(t1, t2), new Point(t3, t4)));
    }
    Set<LongFracPoint> trueIntersections = Sets.newHashSet();
    for (Segment s1 : segments) {
      for (Segment s2 : segments) {
        LongFracPoint intersection = s1.trueIntersection(s2);
        if (intersection != null) {
          trueIntersections.add(intersection);
        }
      }
    }
    return trueIntersections.size();
  }

  private static class Segment {
    private final Point a, b;

    public Segment(Point a, Point b) {
      this.a = a;
      this.b = b;
    }

    public LongFracPoint trueIntersection(Segment that) {
      long D = (this.b.x - this.a.x) * (that.a.y - that.b.y)
          - (that.a.x - that.b.x) * (this.b.y - this.a.y);
      if (D == 0L) {
        return null;
      }
      long x0 = (that.a.x - this.a.x) * (that.a.y - that.b.y)
          - (that.a.x - that.b.x) * (that.a.y - this.a.y);
      long y0 = (this.b.x - this.a.x) * (that.a.y - this.a.y)
          - (that.a.x - this.a.x) * (this.b.y - this.a.y);
      if (signum(x0) * signum(D) == 1
          && signum(y0) * signum(D) == 1
          && abs(x0) < abs(D)
          && abs(y0) < abs(D)) {
        return new LongFracPoint(
            new LongFrac(this.a.x).plus(
                new LongFrac(x0, D).times(new LongFrac(this.b.x).minus(new LongFrac(this.a.x)))),
            new LongFrac(this.a.y).plus(
                new LongFrac(x0, D).times(new LongFrac(this.b.y).minus(new LongFrac(this.a.y)))));
      } else {
        return null;
      }
    }

    @Override
    public String toString() {
      return String.format("[%s->%s]", a, b);
    }
  }

  private static class Point {
    private final long x, y;

    public Point(long x, long y) {
      this.x = x;
      this.y = y;
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
      return String.format("[%d,%d]", x, y);
    }
  }

  private static class LongFracPoint {
    private final LongFrac x, y;

    public LongFracPoint(LongFrac x, LongFrac y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(x, y);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof LongFracPoint) {
        LongFracPoint that = (LongFracPoint) obj;
        return this.x.equals(that.x)
            && this.y.equals(that.y);
      }
      return false;
    }

    @Override
    public String toString() {
      return String.format("[%s,%s]", x, y);
    }
  }

  private static class LongFrac {
    private final long n, d;

    public LongFrac(long n, long d) {
      long g = gcd(n, d);
      this.n = n / g;
      this.d = d / g;
    }

    public LongFrac(long n) {
      this(n, 1);
    }

    private static long gcd(long a, long b) {
      return b == 0 ? a : gcd(b, a % b);
    }

    public LongFrac plus(LongFrac that) {
      return new LongFrac(this.n * that.d + that.n * this.d, this.d * that.d);
    }

    public LongFrac minus(LongFrac that) {
      return new LongFrac(this.n * that.d - that.n * this.d, this.d * that.d);
    }

    public LongFrac times(LongFrac that) {
      return new LongFrac(this.n * that.n, this.d * that.d);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(n, d);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof LongFrac) {
        LongFrac that = (LongFrac) obj;
        return this.n == that.n && this.d == that.d;
      }
      return false;
    }

    @Override
    public String toString() {
      return String.format("%d/%d", n, d);
    }
  }
}
