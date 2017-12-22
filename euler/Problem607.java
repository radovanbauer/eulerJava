package euler;

public class Problem607 {

  public static void main(String[] args) {
    Runner.run(new Problem607()::solve);
  }

  /*
  d1, d2, d3, d4, d5, d6, -(d1+d2+d3+d4+d5+d6)

  A to marsh = marsh to B = (100 - sqrt(2)*50)/2 = 25*(2 - sqrt(2))
  single marsh lane = 10 * sqrt(2)

  x1 = sqrt((25*(2 - sqrt(2)) + d1)^2 + d1^2)
  x2 = sqrt((10 * sqrt(2) + d2)^2 + d2^2)
  x3 = sqrt((10 * sqrt(2) + d3)^2 + d3^2)
  x4 = sqrt((10 * sqrt(2) + d4)^2 + d4^2)
  x5 = sqrt((10 * sqrt(2) + d5)^2 + d5^2)
  x6 = sqrt((10 * sqrt(2) + d6)^2 + d6^2)
  x7 = sqrt((25*(2 - sqrt(2)) - (d1+d2+d3+d4+d5+d6))^2 + (d1+d2+d3+d4+d5+d6)^2)

   */

  public String solve() {
    double[] d = new double[] {0, 0, 0, 0, 0, 0};
    double step = 0.01;
    double t = 0;
    int zeroCount = 0;
    while (true) {
      double[] der = rder(d);
      for (int i = 0; i < 6; i++) {
        d[i] -= step * der[i];
      }
      double newt = time(d);
      if (Math.abs(newt - t) < 1e-15) {
        zeroCount++;
        if (zeroCount > 5) {
          return String.format("%.10f", newt);
        }
      } else {
        zeroCount = 0;
      }
      t = newt;
    }
  }

  private static final double l1 = 25*(2 - Math.sqrt(2));
  private static final double l2 = 10 * Math.sqrt(2);

  private double time(double... d) {

    return Math.sqrt(sq(l1 + d[0] - 0) + sq(d[0] - 0)) / 10
        + Math.sqrt(sq(l2 + d[1] - d[0]) + sq(d[1] - d[0])) / 9
        + Math.sqrt(sq(l2 + d[2] - d[1]) + sq(d[2] - d[1])) / 8
        + Math.sqrt(sq(l2 + d[3] - d[2]) + sq(d[3] - d[2])) / 7
        + Math.sqrt(sq(l2 + d[4] - d[3]) + sq(d[4] - d[3])) / 6
        + Math.sqrt(sq(l2 + d[5] - d[4]) + sq(d[5] - d[4])) / 5
        + Math.sqrt(sq(l1 + 0 - d[5]) + sq(0 - d[5])) / 10;
  }

  private double[] rder(double[] d) {
    return new double[]{
        (l1 + 2 * d[0] - 2 * 0) / Math.sqrt(sq(l1 + d[0] - 0) + sq(d[0] - 0)) / 10
            - (l2 + 2 * d[1] - 2 * d[0]) / Math.sqrt(sq(l2 + d[1] - d[0]) + sq(d[1] - d[0])) / 9,
        (l2 + 2 * d[1] - 2 * d[0]) / Math.sqrt(sq(l2 + d[1] - d[0]) + sq(d[1] - d[0])) / 9
            - (l2 + 2 * d[2] - 2 * d[1]) / Math.sqrt(sq(l2 + d[2] - d[1]) + sq(d[2] - d[1])) / 8,
        (l2 + 2 * d[2] - 2 * d[1]) / Math.sqrt(sq(l2 + d[2] - d[1]) + sq(d[2] - d[1])) / 8
            - (l2 + 2 * d[3] - 2 * d[2]) / Math.sqrt(sq(l2 + d[3] - d[2]) + sq(d[3] - d[2])) / 7,
        (l2 + 2 * d[3] - 2 * d[2]) / Math.sqrt(sq(l2 + d[3] - d[2]) + sq(d[3] - d[2])) / 7
            - (l2 + 2 * d[4] - 2 * d[3]) / Math.sqrt(sq(l2 + d[4] - d[3]) + sq(d[4] - d[3])) / 6,
        (l2 + 2 * d[4] - 2 * d[3]) / Math.sqrt(sq(l2 + d[4] - d[3]) + sq(d[4] - d[3])) / 6
            - (l2 + 2 * d[5] - 2 * d[4]) / Math.sqrt(sq(l2 + d[5] - d[4]) + sq(d[5] - d[4])) / 5,
        (l2 + 2 * d[5] - 2 * d[4]) / Math.sqrt(sq(l2 + d[5] - d[4]) + sq(d[5] - d[4])) / 5
            - (l1 + 2 * 0 - 2 * d[5]) / Math.sqrt(sq(l1 + 0 - d[5]) + sq(0 - d[5])) / 10
    };
  }

  private static final double eps = 1e-10;

  private double[] der(double[] d) {
    double[] res = new double[d.length];
    for (int i = 0; i < res.length; i++) {
      res[i] = der(d, i);
    }
    return res;
  }

  private double der(double[] d, int i) {
    double[] d2 = d.clone();
    d2[i] += eps;
    return (time(d2) - time(d)) / (d2[i] - d[i]);
  }

  private double sq(double d) {
    return d*d;
  }
}
