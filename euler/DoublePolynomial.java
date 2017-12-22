package euler;

import com.google.common.collect.Iterables;

public class DoublePolynomial extends Polynomial<Double, DoublePolynomial> {

  public static final DoublePolynomial ZERO =
      new DoublePolynomial(new Double[] { 0D });

  private DoublePolynomial(Double[] coefs) {
    super(coefs);
  }

  @Override
  protected Field<Double> field() {
    return Field.doubles();
  }

  @Override
  protected DoublePolynomial newInstance(Double[] coefs) {
    return new DoublePolynomial(coefs);
  }

  public static DoublePolynomial create(long... coefs) {
    Double[] doubles = new Double[coefs.length];
    for (int i = 0; i < coefs.length; i++) {
      doubles[i] = (double) coefs[i];
    }
    return new DoublePolynomial(doubles);
  }

  public static DoublePolynomial create(Double... coefs) {
    return new DoublePolynomial(coefs.clone());
  }

  public static DoublePolynomial create(Iterable<Double> coefs) {
    return new DoublePolynomial(Iterables.toArray(coefs, Double.class));
  }
}