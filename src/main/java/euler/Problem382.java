package euler;

import com.google.common.math.LongMath;

public class Problem382 {
  public static void main(String[] args) {
    Runner.run(new Problem382()::solve);
  }

  // g1[1] := 0
  // g1[2] := 0
  // g1[3] := 0
  // g1[n_] := 3*2^(n - 4) - 1 + g1[n - 3] + g2[n - 3]
  // g2[1] := 0
  // g2[2] := 0
  // g2[3] := 0
  // g2[4] := 0
  // g2[n_] := 2^(n - 5) - 1 + g1[n - 4] + g2[n - 4] + g2[n - 3]
  // f[n_] := Sum[g1[i] + g2[i], {i, 1, n}]

  public long solve() {
    long mod = LongMath.pow(10, 9);
    return LongModMatrix2
        .create(new long[][]{
          {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
          {0, 1, 0, 0, 0, 1, 0, 0, 6, -1, 0},
          {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
          {1, 0, 0, 0, 1, 1, 0, 0, 1, -1, 0},
          {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
          {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1}}, mod)
        .pow(LongMath.pow(10, 18) - 3)
        .multiply(LongModMatrix2.create(new long[][] {{0, 0, 0, 2, 0, 0, 0, 0, 1, 1, 0}}, mod).transpose())
        .element(10, 0);
  }
}
