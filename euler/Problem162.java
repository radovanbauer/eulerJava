package euler;

import java.math.BigInteger;
import java.util.Locale;

public class Problem162 {

  public static void main(String[] args) {
    System.out.println(new Problem162().solve(16));
  }

  public String solve(int n) {
    BigInteger total = BigInteger.ZERO;
    for (int digits = 0; digits <= n; digits++) {
      for (int n0 = 1; n0 <= n; n0++) {
        for (int n1 = 1; n1 <= n; n1++) {
          for (int nA = 1; nA <= n; nA++) {
            total = total.add(count(digits, n0, n1, nA));
          }
        }
      }
    }
    return total.toString(16).toUpperCase(Locale.US);
  }

  private BigInteger[][][][] cache = new BigInteger[17][17][17][17];

  private BigInteger count(int digits, int n0, int n1, int nA) {
    if (n0 < 0 || n1 < 0 || nA < 0) {
      return BigInteger.ZERO;
    }
    if (cache[digits][n0][n1][nA] != null) {
      return cache[digits][n0][n1][nA];
    }

    BigInteger result = BigInteger.ZERO;
    if (digits == 0) {
      if (n0 == 0 && n1 == 0 && nA == 0) {
        result = BigInteger.ONE;
      } else {
        result = BigInteger.ZERO;
      }
    } else {
      for (int d = 0; d <= 15; d++) {
        if (d == 0 && digits == 1) {
          continue;
        }
        if (d == 0) {
          result = result.add(count(digits - 1, n0 - 1, n1, nA));
        } else if (d == 1) {
          result = result.add(count(digits - 1, n0, n1 - 1, nA));
        } else if (d == 10) {
          result = result.add(count(digits - 1, n0, n1, nA - 1));
        } else {
          result = result.add(count(digits - 1, n0, n1, nA));
        }
      }
    }
    return cache[digits][n0][n1][nA] = result;
  }
}
