package euler;

import static java.math.BigInteger.ONE;

import java.math.BigInteger;

public class Problem340 {

  public static void main(String[] args) {
    System.out.println(new Problem340().solve());
  }

  public long solve() {
    BigInteger a = BigInteger.valueOf(21).pow(7);
    BigInteger b = BigInteger.valueOf(7).pow(21);
    BigInteger c = BigInteger.valueOf(12).pow(7);
    BigInteger x = b.add(ONE).divide(a);
    BigInteger y = b.add(ONE).mod(a);
    BigInteger res = a.multiply(a.multiply(BigInteger.valueOf(4)).subtract(c.multiply(BigInteger.valueOf(3))))
            .multiply(x.multiply(x.add(ONE)).divide(BigInteger.valueOf(2)))
        .subtract(a.multiply(c).multiply(x))
        .add(y.multiply(x.add(ONE)
            .multiply(a.multiply(BigInteger.valueOf(4)).subtract(c.multiply(BigInteger.valueOf(3)))).subtract(c)))
        .add(b.multiply(b.add(ONE)).divide(BigInteger.valueOf(2)));
    return res.mod(BigInteger.valueOf(1_000_000_000)).longValueExact();
  }
}
