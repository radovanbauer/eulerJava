package euler;

import com.google.common.collect.Iterables;

public class BigFractionPolynomial extends Polynomial<BigFraction, BigFractionPolynomial> {

  public static final BigFractionPolynomial ZERO =
      new BigFractionPolynomial(new BigFraction[] { BigFraction.ZERO });

  private BigFractionPolynomial(BigFraction[] coefs) {
    super(coefs);
  }

  @Override
  protected Field<BigFraction> field() {
    return Field.bigFractions();
  }

  @Override
  protected BigFractionPolynomial newInstance(BigFraction[] coefs) {
    return new BigFractionPolynomial(coefs);
  }

  public static BigFractionPolynomial create(long... coefs) {
    BigFraction[] longFractions = new BigFraction[coefs.length];
    for (int i = 0; i < coefs.length; i++) {
      longFractions[i] = BigFraction.create(coefs[i]);
    }
    return new BigFractionPolynomial(longFractions);
  }

  public static BigFractionPolynomial create(BigFraction... coefs) {
    return new BigFractionPolynomial(coefs.clone());
  }

  public static BigFractionPolynomial create(Iterable<BigFraction> coefs) {
    return new BigFractionPolynomial(Iterables.toArray(coefs, BigFraction.class));
  }
}