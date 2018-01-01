package euler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;

public class Problem267 {

  public static void main(String[] args) {
    System.out.println(new Problem267().solve());
  }

  public String solve() {
    int n = 1000;
    Pascal pascal = new Pascal(n);
    BigInteger max = BigInteger.ZERO;
    for (double f = 0; f <= 1; f += 0.001) {
      max = Ordering.natural().max(max, chance(n, f, pascal));
    }
    System.out.println(new BigDecimal(max).divide(BigDecimal.valueOf(2).pow(n), 12, RoundingMode.HALF_EVEN).toPlainString());
    return "";
  }

  private BigInteger chance(int n, double f, Pascal pascal) {
    BigInteger all = BigInteger.ZERO;
    for (int win = 0; win <= n; win++) {
      int loss = n - win;
      if (win * Math.log(1 + 2 * f) + loss * Math.log(1 - f) >= Math.log(1_000_000_000)) {
        all = all.add(pascal.comb(n, win));
      }
    }
    return all;
  }

  private static class Pascal {
    private final int maxN;
    private final BigInteger[][] comb;

    public Pascal(int maxN) {
      this.maxN = maxN;
      this.comb = new BigInteger[maxN + 1][maxN + 1];
      comb[0][0] = BigInteger.ONE;
      for (int n = 1; n <= maxN; n++) {
        comb[n][0] = comb[n][n] = BigInteger.ONE;
        for (int k = 1; k < n; k++) {
          comb[n][k] = comb[n - 1][k - 1].add(comb[n - 1][k]);
        }
      }
    }

    public BigInteger comb(int n, int k) {
      Preconditions.checkArgument(0 <= n && n <= maxN && 0 <= k && k <= n);
      return comb[n][k];
    }
  }
}
