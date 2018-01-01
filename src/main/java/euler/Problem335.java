package euler;

import com.google.common.math.LongMath;

public class Problem335 {
  public static void main(String[] args) {
    Runner.run(new Problem335()::solve);
  }

  public long solve() {
    // sum[n] == 1/6 (-11 + 3 2^(3 + n) + 2^(3 + 2 n) - 3^(2 + n))
    long n = LongMath.pow(10, 18);
    long mod = LongMath.pow(7, 9);
    return LongMod.create(-11, mod)
        .add(LongMod.create(2, mod).pow(3 + n).multiply(3))
        .add(LongMod.create(2, mod).pow(3 + 2 * n))
        .subtract(LongMod.create(3, mod).pow(2 + n))
        .divide(6)
        .n();
  }
}
