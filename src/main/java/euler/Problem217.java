package euler;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;

public class Problem217 {

  public static void main(String[] args) {
    System.out.println(new Problem217().solve(47));
  }

  public BigInteger solve(int n) {
    BigInteger res = BigInteger.ZERO;
    for (int i = 1; i <= n; i++) {
      res = res.add(sum(i));
    }
    return res.mod(BigInteger.valueOf(3L).pow(15));
  }

  private BigInteger sum(int n) {
    if (n == 0) {
      return BigInteger.ZERO;
    } else if (n == 1) {
      return BigInteger.valueOf(45L);
    } else {
      BigInteger res = ZERO;
      for (int first = 1; first <= 9; first++) {
        for (int last = 0; last <= 9; last++) {
          res = res.add(BigInteger.valueOf(last)
              .add(BigInteger.valueOf(first).multiply(TEN.pow(n - 1)))
              .multiply(count(n - 2, first - last))
              .add(sum(n - 2, first - last).multiply(TEN)));
        }
      }
      return res;
    }
  }

  private BigInteger[][] sumCache = new BigInteger[51][1001];

  private BigInteger sum(int n, int diff) {
    if (sumCache[n][diff + 500] != null) {
      return sumCache[n][diff + 500];
    }
    BigInteger res;
    if (n == 0) {
      res = ZERO;
    } else if (n == 1) {
      res = diff == 0 ? BigInteger.valueOf(45L) : ZERO;
    } else {
      res = ZERO;
      for (int first = 0; first <= 9; first++) {
        for (int last = 0; last <= 9; last++) {
          res = res.add(BigInteger.valueOf(last)
              .add(BigInteger.valueOf(first).multiply(TEN.pow(n - 1)))
              .multiply(count(n - 2, diff + first - last))
              .add(sum(n - 2, diff + first - last).multiply(TEN)));
        }
      }
    }
    return sumCache[n][diff + 500] = res;
  }

  private BigInteger[][] countCache = new BigInteger[51][1001];

  private BigInteger count(int n, int diff) {
    if (countCache[n][diff + 500] != null) {
      return countCache[n][diff + 500];
    }
    BigInteger res;
    if (n == 0) {
      res = diff == 0 ? ONE : ZERO;
    } else if (n == 1) {
      res = diff == 0 ? BigInteger.valueOf(10L) : ZERO;
    } else {
      res = ZERO;
      for (int first = 0; first <= 9; first++) {
        for (int last = 0; last <= 9; last++) {
          res = res.add(count(n - 2, diff + first - last));
        }
      }
    }
    return countCache[n][diff + 500] = res;
  }
}
