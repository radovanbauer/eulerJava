package euler;

import com.google.common.math.LongMath;

public class Problem405v2 {

  public static void main(String[] args) {
    System.out.println(new Problem405().solve());
  }

  public long solve() {
    // f(n) = 1/15 (6*4^n - 20*2^n - (-1)^n + 15)
    long mod = LongMath.pow(17, 7);
    long modTotient = 16 * LongMath.pow(17, 6);
    long exp = LongMod.create(10, modTotient).pow(LongMath.pow(10, 18)).n();
    return LongMod.create(4, mod).pow(exp).multiply(6)
        .subtract(LongMod.create(2, mod).pow(exp).multiply(20))
        .subtract(LongMod.create(-1, mod).pow(exp))
        .add(15)
        .divide(15).n();
  }
}
