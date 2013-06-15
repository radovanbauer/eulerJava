package euler;

import static java.lang.Math.abs;
import static java.lang.Math.sin;

import java.util.Arrays;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

public class Problem177v2 {

  public static void main(String[] args) {
    System.out.println(new Problem177v2().solve());
  }

  public int solve() {
    int cnt = 0;
    for (int a1 = 1; a1 < 180; a1++) {
      System.out.println(a1);
      for (int a2 = 1; a1 + a2 < 180; a2++) {
        for (int c1 = 1; c1 < 180 && a2 + c1 < 180; c1++) {
          for (int c2 = 1; c1 + c2 < 180 && a1 + c2 < 180; c2++) {
            int b1max = Math.min(180, Math.min(180 - a2 - c1, 180 - a1 - a2));
            for (int b1 = 1; b1 < b1max; b1++) {
              int b2 = 180 - a2 - b1 - c1;
              int d1 = 180 - b2 - c1 - c2;
              int d2 = 180 - b1 - a1 - a2;
              if (b2 <= 0 || b2 >= 180) continue;
              if (d1 <= 0 || d1 >= 180) continue;
              if (d2 <= 0 || d2 >= 180) continue;
              if (b1 + b2 >= 180) continue;
              if (d1 + d2 >= 180) continue;
              int[] solution = { a1, a2, b1, b2, c1, c2, d1, d2 };
              int[] normalized = ARRAY_ORDERING.min(
                  new int[] { a1, a2, b1, b2, c1, c2, d1, d2 },
                  new int[] { b1, b2, c1, c2, d1, d2, a1, a2 },
                  new int[] { c1, c2, d1, d2, a1, a2, b1, b2 },
                  new int[] { d1, d2, a1, a2, b1, b2, c1, c2 },
                  new int[] { d2, d1, c2, c1, b2, b1, a2, a1 },
                  new int[] { c2, c1, b2, b1, a2, a1, d2, d1 },
                  new int[] { b2, b1, a2, a1, d2, d1, c2, c1 },
                  new int[] { a2, a1, d2, d1, c2, c1, b2, b1 });
              if (!Arrays.equals(solution, normalized)) continue;
              if (abs(sin(a1) * sin(b1) * sin(c1) * sin(d1)
                  - sin(a2) * sin(b2) * sin(c2) * sin(d2)) > 1e-9) continue;
              System.out.println(String.format("%d: %s", cnt, Arrays.toString(normalized)));
              cnt++;
            }
          }
        }
      }
    }
    return cnt;
  }

  private static final Ordering<int[]> ARRAY_ORDERING = new Ordering<int[]>() {
    @Override
    public int compare(int[] left, int[] right) {
      int minLength = Math.min(left.length, right.length);
      for (int i = 0; i < minLength; i++) {
        if (left[i] < right[i]) return -1;
        else if (left[i] > right[i]) return 1;
      }
      return Ints.compare(left.length, right.length);
    }
  };
}
