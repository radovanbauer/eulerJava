package euler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class Problem323 {

  public static void main(String[] args) {
    System.out.println(new Problem323().solve());
  }

  public double solve() {
    long[][] C = new long[33][33];
    for (int n = 0; n <= 32; n++) {
      for (int k = 0; k <= n; k++) {
        if (k == 0 || k == n) {
          C[n][k] = 1;
        } else {
          C[n][k] = C[n - 1][k - 1] + C[n - 1][k];
        }
      }
    }
    double expected = 0;
    BigInteger two = BigInteger.valueOf(2);
    expected += BigDecimal.ONE.divide(new BigDecimal(two.pow(32))).doubleValue();
    for (int k = 2; k <= 100; k++) {
      BigInteger den = BigInteger.ZERO;
      for (int i = 0; i <= 31; i++) {
        den = den.add(BigInteger.valueOf(C[32][i]).multiply(two.pow(k).subtract(two).pow(i)));
      }
      expected += k * new BigDecimal(den).divide(new BigDecimal(two.pow(k * 32)), MathContext.DECIMAL128).doubleValue();
      System.out.println(expected);
    }
    return expected;
  }
}
