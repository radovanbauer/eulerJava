package euler;

import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Ordering;

public class Problem177 {

  public static void main(String[] args) {
    System.out.println(new Problem177().solve());
  }

  public int solve() {
    int cnt = 0;
    for (int a1 = 1; a1 <= 45; a1++) {
      System.out.println(a1);
      for (int a2 = 1; a1 + a2 < 180; a2++) {
        for (int c1 = 1; c1 < 180 && a2 + c1 < 180; c1++) {
          for (int c2 = 1; c1 + c2 < 180 && a1 + c2 < 180; c2++) {
            int b = 180 - a2 - c1;
            int d = 180 - c2 - a1;
            double ab = 1D * sin(toRadians(c1)) / sin(toRadians(b));
            double da = 1D * sin(toRadians(c2)) / sin(toRadians(d));
            double bd = sqrt(ab * ab + da * da - 2 * ab * da * cos(toRadians(a1 + a2)));
            double b1d = toDegrees(acos((ab * ab + bd * bd - da * da) / (2 * ab * bd)));
            if (abs(round(b1d) - b1d) < 1e-9) {
              int b1 = (int) round(b1d);
              int b2 = 180 - a2 - b1 - c1;
              int d1 = 180 - b2 - c1 - c2;
              int d2 = 180 - b1 - a1 - a2;
              List<Integer> normalized = Ordering.natural().<Integer>lexicographical().min(
                  Arrays.asList(a1, a2, b1, b2, c1, c2, d1, d2),
                  Arrays.asList(b1, b2, c1, c2, d1, d2, a1, a2),
                  Arrays.asList(c1, c2, d1, d2, a1, a2, b1, b2),
                  Arrays.asList(d1, d2, a1, a2, b1, b2, c1, c2),
                  Arrays.asList(d2, d1, c2, c1, b2, b1, a2, a1),
                  Arrays.asList(c2, c1, b2, b1, a2, a1, d2, d1),
                  Arrays.asList(b2, b1, a2, a1, d2, d1, c2, c1),
                  Arrays.asList(a2, a1, d2, d1, c2, c1, b2, b1));
              if (Arrays.asList(a1, a2, b1, b2, c1, c2, d1, d2).equals(normalized)) {
                cnt++;
              }
            }
          }
        }
      }
    }
    return cnt;
  }
}
