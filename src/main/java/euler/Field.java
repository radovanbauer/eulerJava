package euler;

public abstract class Field<T> extends Domain<T> {

  public abstract T invert(T t);

  public T divide(T t1, T t2) {
    return multiply(t1, invert(t2));
  }

  public static Field<LongFraction> longFractions() {
    return LongFractionField.INSTANCE;
  }

  private static class LongFractionField extends Field<LongFraction> {
    private static final LongFractionField INSTANCE = new LongFractionField();

    @Override
    public Class<LongFraction> clazz() {
      return LongFraction.class;
    }
    
    @Override
    public LongFraction zero() {
      return LongFraction.ZERO;
    }

    @Override
    public LongFraction one() {
      return LongFraction.ONE;
    }

    @Override
    public LongFraction negate(LongFraction t) {
      return t.negate();
    }

    @Override
    public LongFraction invert(LongFraction t) {
      return t.invert();
    }

    @Override
    public LongFraction add(LongFraction t1, LongFraction t2) {
      return t1.add(t2);
    }

    @Override
    public LongFraction multiply(LongFraction t1, LongFraction t2) {
      return t1.multiply(t2);
    }

    @Override
    public LongFraction get(long n) {
      return LongFraction.create(n);
    }
  }


  public static Field<BigFraction> bigFractions() {
    return BigFractionField.INSTANCE;
  }

  private static class BigFractionField extends Field<BigFraction> {
    private static final BigFractionField INSTANCE = new BigFractionField();

    @Override
    public Class<BigFraction> clazz() {
      return BigFraction.class;
    }

    @Override
    public BigFraction zero() {
      return BigFraction.ZERO;
    }

    @Override
    public BigFraction one() {
      return BigFraction.ONE;
    }

    @Override
    public BigFraction negate(BigFraction t) {
      return t.negate();
    }

    @Override
    public BigFraction invert(BigFraction t) {
      return t.invert();
    }

    @Override
    public BigFraction add(BigFraction t1, BigFraction t2) {
      return t1.add(t2);
    }

    @Override
    public BigFraction multiply(BigFraction t1, BigFraction t2) {
      return t1.multiply(t2);
    }

    @Override
    public BigFraction get(long n) {
      return BigFraction.create(n);
    }
  }


  public static Field<Double> doubles() {
    return DoubleField.INSTANCE;
  }

  private static class DoubleField extends Field<Double> {
    private static final DoubleField INSTANCE = new DoubleField();

    @Override
    public Class<Double> clazz() {
      return Double.class;
    }

    @Override
    public Double zero() {
      return 0D;
    }

    @Override
    public Double one() {
      return 1D;
    }

    @Override
    public Double negate(Double t) {
      return -t;
    }

    @Override
    public Double invert(Double t) {
      return 1D / t;
    }

    @Override
    public Double add(Double t1, Double t2) {
      return t1 + t2;
    }

    @Override
    public Double multiply(Double t1, Double t2) {
      return t1 * t2;
    }

    @Override
    public Double subtract(Double t1, Double t2) {
      return t1 - t2;
    }

    @Override
    public Double divide(Double t1, Double t2) {
      return t1 / t2;
    }

    @Override
    public Double get(long n) {
      return (double) n;
    }
  }
}
