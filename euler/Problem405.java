package euler;

import com.google.common.math.LongMath;

public class Problem405 {

  public static void main(String[] args) {
    System.out.println(new Problem405().solve());
  }

  public long solve() {
    long mod = LongMath.pow(17, 7);
    long modTotient = 16 * LongMath.pow(17, 6);
    LongModMatrix m = LongModMatrix.create(new long[][] {
        {4, 1, 0, 4, 0, 0},
        {0, 1, 2, 0, 0, 2},
        {0, 1, 0, 0, 0, 0},
        {0, 0, 0, 1, 2, 1},
        {0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 1}}, mod);
    LongModMatrix a = LongModMatrix.create(new long[][] {{0, 2, 0, 0, 0, 1}}, mod).transpose();
    long exp = LongMod.create(10, modTotient).pow(LongMath.pow(10, 18)).subtract(1).n();
    return m.pow(exp).multiply(a).element(0, 0).n();
  }
}
