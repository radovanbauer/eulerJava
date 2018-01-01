package euler;

import com.google.common.math.LongMath;

public class Problem599 {
  public static void main(String[] args) {
    Runner.run(new Problem599()::solve2);
  }

  public long solve() {
    int colors = 10;
    int blocks = 8;
    int same = colors;
    int diff = colors * (colors - 1) + colors * (colors - 1) * (colors - 2) / 3;
    long[][] sameStart = new long[blocks + 1][same + diff + 1];
    long[][] diffStart = new long[blocks + 1][same + diff + 1];
    for (int first = 1; first <= same; first++) {
      sameStart[1][first] = 1;
    }
    for (int first = same + 1; first <= same + diff; first++) {
      diffStart[1][first] = 1;
    }
    for (int b = 2; b <= blocks; b++) {
      for (int last = 1; last <= same + diff; last++) {
        for (int prevLast = 1; prevLast <= last; prevLast++) {
          sameStart[b][last] += sameStart[b - 1][prevLast];
          diffStart[b][last] += diffStart[b - 1][prevLast];
        }
      }
    }
    long sum = 0;
    for (int last = 1; last <= same + diff; last++) {
      sum += sameStart[blocks][last];
      sum += 3 * diffStart[blocks][last];
    }
    return sum;
  }

  public long solve2() {
    // Sum[Binomial[6 + i, 7], {i, 1 + (c (c^2 - 1))/3, c + (c (c^2 - 1))/3}]
    //   + 3*Sum[Binomial[6 + i, 7], {i, 1, (c (c^2 - 1))/3}]
    // Binomial[7 + (c (c^2 + 2))/3, 8] + 2*Binomial[7 + (c (c^2 - 1))/3, 8]
    int c = 10;
    return LongMath.binomial(7 + c * (c * c + 2) / 3, 8) + 2 * LongMath.binomial(7 + c * (c * c - 1) / 3, 8);
  }
}
