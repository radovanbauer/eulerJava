package euler;

import static com.google.common.math.DoubleMath.fuzzyEquals;

public class Problem613 {

  private static final double EPS = 1e-20;

  public static void main(String[] args) {
    Runner.run(new Problem613()::solve);
  }

  public double solve() {
    for (double step = 1;; step /= 2) {
      System.out.println(solve(step));
    }
  }

  private double solve(double step) {
    long points = 0;
    double psum = 0;
    for (double x = 0; x <= 4; x += step) {
      for (double y = 0; y <= 3; y += step) {
        double x1 = 4 - x;
        double y1 = 0 - y;
        double x2 = 0 - x;
        double y2 = 3 - y;
        if (fuzzyEquals(x1, 0, EPS) && fuzzyEquals(y1, 0, EPS)) {
          x1 = 1;
          y1 = 0;
        }
        if (fuzzyEquals(x2, 0, EPS) && fuzzyEquals(y2, 0, EPS)) {
          x2 = 0;
          y2 = 1;
        }
        points++;
        double norm1 = Math.sqrt(x1 * x1 + y1 * y1);
        double norm2 = Math.sqrt(x2 * x2 + y2 * y2);
        double angle = Math.acos((x1 * x2 + y1 * y2) / (norm1 * norm2));
        psum += angle / (2 * Math.PI);
//        System.out.println(r + " " + c + ": " + angle + " " + (x1 * x2 + y1 * y2) + " "+ norm1 + " " + norm2);
      }
    }
    return psum / points;
  }
}
