package euler;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.function.DoubleUnaryOperator;

public class Problem449 {

  public static void main(String[] args) {
    System.out.printf("%.8f\n", new Problem449().solve());
  }

  public double solve() {
    double v = integrate(this::f, 0D, 1D, 1_000_000);
    return 2D * Math.PI * v - 4D / 3D * Math.PI * 3D * 3D * 1D;
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

  private double f(double z) {
    return sq(3*sqrt(1 - z*z) + sqrt(1 - z*z)/sqrt(1 + 8*z*z))
        * (1 - 24*z*z/pow(1 + 8*z*z, 1.5D) + 3/sqrt(1 + 8*z*z));
  }

  private double sq(double x) {
    return x*x;
  }
}
