package euler;

public class DoubleMatrix extends Matrix<Double, DoubleMatrix> {

  private final Domain<Double> domain;

  private DoubleMatrix(Double[][] m, Domain<Double> domain) {
    super(m);
    this.domain = domain;
  }

  public static DoubleMatrix create(double[][] m) {
    Double[][] copy = new Double[m.length][];
    for (int row = 0; row < m.length; row++) {
      copy[row] = new Double[m[row].length];
      for (int column = 0; column < m[row].length; column++) {
        copy[row][column] = m[row][column];
      }
    }
    return new DoubleMatrix(copy, Domain.doubles());
  }

  @Override
  protected Domain<Double> domain() {
    return domain;
  }

  @Override
  protected DoubleMatrix newInstance(Double[][] m) {
    return new DoubleMatrix(m, domain);
  }
}
