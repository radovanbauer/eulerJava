package euler;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;

public class Problem359 {

  private static final long MOD = 100_000_000;
  private static final BigInteger TWO = BigInteger.valueOf(2);
  private static final BigInteger THREE = BigInteger.valueOf(3);
  private static final BigInteger FOUR = BigInteger.valueOf(4);

  public static void main(String[] args) {
    System.out.println(new Problem359().solve());
  }

  public long solve() {
    BigInteger sum = ZERO;
    long prod = 71328803586048L;
    for (long d = 1; d * d <= prod; d++) {
      if (prod % d == 0) {
        sum = sum.add(P(BigInteger.valueOf(d), BigInteger.valueOf(prod / d)));
        if (d * d < prod) {
          sum = sum.add(P(BigInteger.valueOf(prod / d), BigInteger.valueOf(d)));
        }
      }
    }
    return sum.mod(BigInteger.valueOf(MOD)).longValueExact();
  }

  private BigInteger P(BigInteger f, BigInteger r) {
    BigInteger start;
    BigInteger delta1;
    BigInteger delta2;
    if (f.compareTo(ONE) == 0) {
      start = ONE;
      delta1 = TWO;
      delta2 = THREE;
    } else if (f.mod(TWO).compareTo(ZERO) == 0) {
      start = f.multiply(f).divide(TWO);
      delta1 = f.multiply(TWO).add(ONE);
      delta2 = TWO;
    } else {
      start = f.multiply(f).subtract(ONE).divide(TWO);
      delta1 = ONE;
      delta2 = f.multiply(TWO);
    }
    if (r.mod(TWO).compareTo(ZERO) == 0) {
      return start.add(delta1.add(delta2).add(r.subtract(FOUR))
          .multiply(r.subtract(TWO)).divide(TWO)).add(delta1).add(r).subtract(TWO);
    } else {
      return start.add(delta1.add(delta2).add(r.subtract(THREE))
          .multiply(r.subtract(ONE)).divide(TWO));
    }
  }
}
