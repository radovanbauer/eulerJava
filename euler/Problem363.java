package euler;

import java.util.function.DoubleUnaryOperator;

import com.google.auto.value.AutoValue;

public class Problem363 {

  public static void main(String[] args) {
    System.out.println(String.format("%.10f", new Problem363().solve()));
  }

  private static final double EPS = 1e-15;

  public double solve() {
    double high = 1;
    double low = 0;
    while (high - low > EPS) {
      double mid = (high + low) / 2;
      CubicBezier bezier = CubicBezier.create(
          Point.create(1, 0), Point.create(1, mid), Point.create(mid, 1), Point.create(0, 1));
      double volume = Math.abs(bezier.area());
      double diff = volume - Math.PI / 4;
      if (diff > 0) {
        high = mid;
      } else {
        low = mid;
      }
    }
    double v = low;
    CubicBezier bezier = CubicBezier.create(
        Point.create(1, 0), Point.create(1, v), Point.create(v, 1), Point.create(0, 1));
    return 100 * (2 * bezier.length() / Math.PI - 1);
  }

  @AutoValue
  static abstract class CubicBezier {
    abstract Point p0();
    abstract Point p1();
    abstract Point p2();
    abstract Point p3();
    abstract DoublePolynomial xPoly();
    abstract DoublePolynomial yPoly();

    static CubicBezier create(Point p0, Point p1, Point p2, Point p3) {
      DoublePolynomial oneMinusT = DoublePolynomial.create(1, -1);
      DoublePolynomial t = DoublePolynomial.create(0, 1);
      DoublePolynomial xPoly = oneMinusT.multiply(oneMinusT).multiply(oneMinusT).multiply(p0.x())
          .add(oneMinusT.multiply(oneMinusT).multiply(t).multiply(3 * p1.x()))
          .add(oneMinusT.multiply(t).multiply(t).multiply(3 * p2.x()))
          .add(t.multiply(t).multiply(t).multiply(p3.x()));
      DoublePolynomial yPoly = oneMinusT.multiply(oneMinusT).multiply(oneMinusT).multiply(p0.y())
          .add(oneMinusT.multiply(oneMinusT).multiply(t).multiply(3 * p1.y()))
          .add(oneMinusT.multiply(t).multiply(t).multiply(3 * p2.y()))
          .add(t.multiply(t).multiply(t).multiply(p3.y()));
      return new AutoValue_Problem363_CubicBezier(p0, p1, p2, p3, xPoly, yPoly);
    }

    double area() {
      return yPoly().multiply(xPoly().derive()).integrate(0D, 1D);
    }

    double length() {
      DoublePolynomial xd = xPoly().derive();
      DoublePolynomial yd = yPoly().derive();
      return integrate(t -> Math.sqrt(xd.multiply(xd).add(yd.multiply(yd)).evaluate(t)),
          0D, 1D, 1_000_000);
    }

    private double integrate(DoubleUnaryOperator f, double start, double end, int steps) {
      double res = 0;
      for (double step = 0; step < steps; step++) {
        double z1 = start * (1 - step / steps) + end * (step / steps);
        double z2 = start * (1 - (step + 1) / steps) + end * ((step + 1) / steps);
        res += 0.5D * (f.applyAsDouble(z1) + f.applyAsDouble(z2)) * (z2 - z1);
      }
      return res;
    }
  }

  @AutoValue
  static abstract class Point {
    abstract double x();
    abstract double y();

    static Point create(double x, double y) {
      return new AutoValue_Problem363_Point(x, y);
    }

    Point add(Point that) {
      return create(this.x() + that.x(), this.y() + that.y());
    }

    Point multiply(double d) {
      return create(this.x() * d, this.y() * d);
    }
  }
}
