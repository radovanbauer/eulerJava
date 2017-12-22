package euler;

import java.math.BigInteger;

public class BigIntegerMatrix extends Matrix<BigInteger, BigIntegerMatrix> {

  private final Domain<BigInteger> domain;

  private BigIntegerMatrix(BigInteger[][] m, Domain<BigInteger> domain) {
    super(m);
    this.domain = domain;
  }

  public static BigIntegerMatrix create(BigInteger[][] m) {
    BigInteger[][] copy = new BigInteger[m.length][];
    for (int row = 0; row < m.length; row++) {
      copy[row] = new BigInteger[m[row].length];
      for (int column = 0; column < m[row].length; column++) {
        copy[row][column] = m[row][column];
      }
    }
    return new BigIntegerMatrix(copy, Domain.bigIntegers());
  }

  public static BigIntegerMatrix create(long[][] m) {
    BigInteger[][] copy = new BigInteger[m.length][];
    for (int row = 0; row < m.length; row++) {
      copy[row] = new BigInteger[m[row].length];
      for (int column = 0; column < m[row].length; column++) {
        copy[row][column] = BigInteger.valueOf(m[row][column]);
      }
    }
    return new BigIntegerMatrix(copy, Domain.bigIntegers());
  }

  @Override
  protected Domain<BigInteger> domain() {
    return domain;
  }

  @Override
  protected BigIntegerMatrix newInstance(BigInteger[][] m) {
    return new BigIntegerMatrix(m, domain);
  }
}
