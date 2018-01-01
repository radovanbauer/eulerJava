package euler;

import static java.math.RoundingMode.CEILING;

import com.google.common.math.LongMath;

public class Problem206v2 {

  public static void main(String[] args) {
    System.out.println(new Problem206v2().solve());
  }

  public long solve() {
    long minSqrt = LongMath.sqrt(1020304050607080900L, CEILING);
    for (long n = minSqrt;; n++) {
      char[] chars = String.valueOf(n * n).toCharArray();
      if (chars[0] == '1' &&
          chars[2] == '2' &&
          chars[4] == '3' &&
          chars[6] == '4' &&
          chars[8] == '5' &&
          chars[10] == '6' &&
          chars[12] == '7' &&
          chars[14] == '8' &&
          chars[16] == '9' &&
          chars[18] == '0') {
        return n;
      }
    }
  }
}
