package euler;

import java.math.RoundingMode;

import com.google.common.math.LongMath;

public class Problem509 {

  public static void main(String[] args) {
    System.out.println(new Problem509().solve());
  }

  public long solve() {
    long max = 123456787654321L;
    int mod = 1234567890;
    int max2Exp = LongMath.log2(max, RoundingMode.FLOOR);
    long[] grundyCount = new long[max2Exp + 1];
    grundyCount[0] = max;
    for (int exp = 1; exp <= max2Exp; exp++) {
      grundyCount[exp] = max / (1L << exp);
      grundyCount[exp - 1] -= grundyCount[exp];
    }
    LongMod winning = LongMod.zero(mod);
    for (int a = 0; a <= max2Exp; a++) {
      for (int b = 0; b <= max2Exp; b++) {
        for (int c = 0; c <= max2Exp; c++) {
          if ((a ^ b ^ c) != 0) {
            winning = winning.add(
                LongMod.create(grundyCount[a], mod).multiply(grundyCount[b]).multiply(grundyCount[c]));
          }
        }
      }
    }
    return winning.n();
  }
}
