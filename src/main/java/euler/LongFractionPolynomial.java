package euler;

import com.google.common.collect.Iterables;

public class LongFractionPolynomial extends Polynomial<LongFraction, LongFractionPolynomial> {

  public static final LongFractionPolynomial ZERO =
      new LongFractionPolynomial(new LongFraction[] { LongFraction.ZERO });

  private LongFractionPolynomial(LongFraction[] coefs) {
    super(coefs);
  }

  @Override
  protected Field<LongFraction> field() {
    return Field.longFractions();
  }

  @Override
  protected LongFractionPolynomial newInstance(LongFraction[] coefs) {
    return new LongFractionPolynomial(coefs);
  }

  public static LongFractionPolynomial create(long... coefs) {
    LongFraction[] longFractions = new LongFraction[coefs.length];
    for (int i = 0; i < coefs.length; i++) {
      longFractions[i] = LongFraction.create(coefs[i]);
    }
    return new LongFractionPolynomial(longFractions);
  }

  public static LongFractionPolynomial create(LongFraction... coefs) {
    return new LongFractionPolynomial(coefs.clone());
  }

  public static LongFractionPolynomial create(Iterable<LongFraction> coefs) {
    return new LongFractionPolynomial(Iterables.toArray(coefs, LongFraction.class));
  }
}