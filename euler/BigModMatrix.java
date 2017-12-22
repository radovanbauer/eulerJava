package euler;

import java.math.BigInteger;

public class BigModMatrix extends Matrix<BigMod, BigModMatrix> {

  private final Domain<BigMod> domain;

  private BigModMatrix(BigMod[][] m, Domain<BigMod> domain) {
    super(m);
    this.domain = domain;
  }

  public static BigModMatrix create(BigMod[][] m, BigInteger mod) {
    BigMod[][] copy = new BigMod[m.length][];
    for (int row = 0; row < m.length; row++) {
      copy[row] = m[row].clone();
    }
    return new BigModMatrix(copy, Domain.bigMods(mod));
  }

  public static BigModMatrix create(long[][] m, long mod) {
    BigMod[][] copy = new BigMod[m.length][];
    for (int row = 0; row < m.length; row++) {
      copy[row] = new BigMod[m[row].length];
      for (int column = 0; column < m[row].length; column++) {
        copy[row][column] = BigMod.create(m[row][column], mod);
      }
    }
    return new BigModMatrix(copy, Domain.bigMods(BigInteger.valueOf(mod)));
  }

  @Override
  protected Domain<BigMod> domain() {
    return domain;
  }

  @Override
  protected BigModMatrix newInstance(BigMod[][] m) {
    return new BigModMatrix(m, domain);
  }
}
