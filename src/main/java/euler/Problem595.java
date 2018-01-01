package euler;

import com.google.common.math.BigIntegerMath;

import java.math.BigInteger;
import java.util.Arrays;

public class Problem595 {

  private static final int MAX = 53;

  public static void main(String[] args) {
    new Problem595().solve();
  }

  public void solve() {
    BigFraction[] s = new BigFraction[MAX];
    s[0] = BigFraction.ZERO;
    s[1] = BigFraction.ZERO;
    for (int n = 2; n < MAX; n++) {
      BigFraction sum = BigFraction.create(segmentCount(n, n));
      for (int k = 2; k < n; k++) {
        sum = sum.add(BigFraction.create(segmentCount(n, k)).multiply(s[k].add(1)));
      }
      s[n] = sum.divide(BigFraction.create(BigIntegerMath.factorial(n).subtract(segmentCount(n, n))));
    }
    System.out.printf("%.8f\n", s[MAX - 1].doubleValue());
  }


  private BigInteger segmentCount(int n, int segments) {
    return comb[n - 1][segments - 1].multiply(a[segments - 1]);
  }

  private static BigInteger[] a = new BigInteger[MAX];

  static {
    a[0] = BigInteger.ONE;
    for (int n = 1; n < MAX; n++) {
      a[n] = a[n - 1].multiply(BigInteger.valueOf(n * (n + 2))).add(BigInteger.valueOf((n & 1) == 0 ? 1 : -1)).divide(BigInteger.valueOf(n + 1));
    }
  }

  private static BigInteger[][] comb = new BigInteger[MAX][MAX];

  static {
    for (int n = 0; n < MAX; n++) {
      Arrays.fill(comb[n], BigInteger.ZERO);
      comb[n][0] = BigInteger.ONE;
      for (int k = 1; k <= n; k++) {
        comb[n][k] = comb[n - 1][k - 1].add(comb[n - 1][k]);
      }
    }
  }
}
