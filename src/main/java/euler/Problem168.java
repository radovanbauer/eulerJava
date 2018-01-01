package euler;

import static java.math.BigInteger.TEN;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;

public class Problem168 {

  public static void main(String[] args) {
    System.out.println(new Problem168().solve(100));
  }

  public long solve(int max) {
    long sum = 0L;
    for (int n = 1; n < max; n++) {
      for (int l = 1; l <= 9; l++) {
        for (int m = 1; m <= 9; m++) {
          BigInteger x = TEN.pow(n).subtract(BigInteger.valueOf(m)).multiply(BigInteger.valueOf(l));
          if (x.mod(BigInteger.valueOf(10 * m - 1)).compareTo(ZERO) == 0) {
            BigInteger k = x.divide(BigInteger.valueOf(10 * m - 1));
            if (k.toString().length() == n) {
              BigInteger z = k.multiply(TEN).add(BigInteger.valueOf(l));
              sum = BigInteger.valueOf(sum).add(z).mod(BigInteger.valueOf(100000)).longValue();
            }
          }
        }
      }
    }
    return sum;
  }
}
