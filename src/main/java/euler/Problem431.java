package euler;

import static java.math.RoundingMode.FLOOR;

import com.google.common.math.IntMath;

public class Problem431 {

  public static void main(String[] args) {
    System.out.println(new Problem431().solve(6, 40));
//    23.38602904768777
  }

  public double solve(double r, double alpha) {
    double minv = v2(r, alpha, 0);
    double maxv = v2(r, alpha, r);
    double sum = 0D;
    System.out.println(v2(3, 30, 1.114785284));
//    for (int volume = (int) Math.ceil(minv); volume <= (int) Math.floor(maxv); volume++) {
//      if (isSquare(volume)) {
//        sum += find(r, alpha, volume);
//      }
//    }
    return sum;
  }

  private boolean isSquare(int n) {
    int sqrtFloor = IntMath.sqrt(n, FLOOR);
    return sqrtFloor * sqrtFloor == n;
  }

  private double find(double r, double alpha, double volume) {
    return find(r, alpha, volume, 0, r);
  }

  private double find(double r, double alpha, double volume, double min, double max) {
    if (Math.abs(max - min) < 1e-12) {
      return min;
    } else {
      double mid = (min + max) / 2;
      System.out.println(mid);
      double v = v2(r, alpha, mid);
      if (v < volume) {
        return find(r, alpha, volume, mid, max);
      } else {
        return find(r, alpha, volume, min, mid);
      }
    }
  }

  private double v2(double r, double alpha, double x) {
    double volume = 0D;
    double tanAlpha = Math.tan(Math.toRadians(alpha));
    int divs = 10000000;
    double h = (r + x) / divs;
    for (int i = 0; i <= divs; i++) {
      double z = i * h;
      double phi;
      if (z <= r - x) {
        phi = 2 * Math.PI;
      } else {
        phi = 2 * Math.acos((x * x + z * z - r * r) / (2 * x * z));
      }
      double a = z * z * phi;
      if (i == 0 || i == divs) {
        volume += a;
      } else if (i % 2 == 0) {
        volume += 2 * a;
      } else {
        volume += 4 * a;
      }
    }
    return volume * tanAlpha * h / 3;
  }
}
