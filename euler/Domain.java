package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigInteger;

public abstract class Domain<T> {

  public abstract Class<T> clazz();

  public abstract T zero();

  public abstract T one();

  public abstract T negate(T t);

  public abstract T add(T t1, T t2);

  public abstract T multiply(T t1, T t2);

  public T subtract(T t1, T t2) {
    return add(t1, negate(t2));
  }

  public abstract T get(long n);

  public static Domain<Long> longs() {
    return LongDomain.INSTANCE;
  }

  private static class LongDomain extends Domain<Long> {
    private static final LongDomain INSTANCE = new LongDomain();

    @Override
    public Class<Long> clazz() {
      return Long.class;
    }
    
    @Override
    public Long zero() {
      return 0L;
    }

    @Override
    public Long one() {
      return 1L;
    }

    @Override
    public Long negate(Long t) {
      return -t;
    }

    @Override
    public Long add(Long t1, Long t2) {
      return t1 + t2;
    }

    @Override
    public Long multiply(Long t1, Long t2) {
      return t1 * t2;
    }

    @Override
    public Long get(long n) {
      return n;
    }
  }

  public static Domain<BigInteger> bigIntegers() {
    return BigIntegerDomain.INSTANCE;
  }

  private static class BigIntegerDomain extends Domain<BigInteger> {
    private static final BigIntegerDomain INSTANCE = new BigIntegerDomain();

    @Override
    public Class<BigInteger> clazz() {
      return BigInteger.class;
    }
    
    @Override
    public BigInteger zero() {
      return BigInteger.ZERO;
    }

    @Override
    public BigInteger one() {
      return BigInteger.ONE;
    }

    @Override
    public BigInteger negate(BigInteger t) {
      return t.negate();
    }

    @Override
    public BigInteger add(BigInteger t1, BigInteger t2) {
      return t1.add(t2);
    }

    @Override
    public BigInteger multiply(BigInteger t1, BigInteger t2) {
      return t1.multiply(t2);
    }

    @Override
    public BigInteger get(long n) {
      return BigInteger.valueOf(n);
    }
  }


  public static Domain<LongMod> longMods(long mod) {
    return new LongModDomain(mod);
  }

  private static class LongModDomain extends Domain<LongMod> {
    private final long mod;

    private LongModDomain(long mod) {
      checkArgument(mod >= 1);
      this.mod = mod;
    }

    @Override
    public Class<LongMod> clazz() {
      return LongMod.class;
    }
    
    @Override
    public LongMod zero() {
      return LongMod.zero(mod);
    }

    @Override
    public LongMod one() {
      return LongMod.create(1, mod);
    }

    @Override
    public LongMod negate(LongMod t) {
      return t.negate();
    }

    @Override
    public LongMod add(LongMod t1, LongMod t2) {
      return t1.add(t2);
    }

    @Override
    public LongMod multiply(LongMod t1, LongMod t2) {
      return t1.multiply(t2);
    }

    @Override
    public LongMod get(long n) {
      return LongMod.create(n, mod);
    }
  }

  public static Domain<BigMod> bigMods(BigInteger mod) {
    return new BigModDomain(mod);
  }

  private static class BigModDomain extends Domain<BigMod> {
    private final BigInteger mod;

    private BigModDomain(BigInteger mod) {
      checkArgument(mod.compareTo(BigInteger.ZERO) > 0);
      this.mod = mod;
    }

    @Override
    public Class<BigMod> clazz() {
      return BigMod.class;
    }
    
    @Override
    public BigMod zero() {
      return BigMod.zero(mod);
    }

    @Override
    public BigMod one() {
      return BigMod.create(BigInteger.ONE, mod);
    }

    @Override
    public BigMod negate(BigMod t) {
      return t.negate();
    }

    @Override
    public BigMod add(BigMod t1, BigMod t2) {
      return t1.add(t2);
    }

    @Override
    public BigMod multiply(BigMod t1, BigMod t2) {
      return t1.multiply(t2);
    }

    @Override
    public BigMod get(long n) {
      return BigMod.create(BigInteger.valueOf(n), mod);
    }
  }

  public static Domain<Double> doubles() {
    return DoubleDomain.INSTANCE;
  }

  private static class DoubleDomain extends Domain<Double> {
    private static final DoubleDomain INSTANCE = new DoubleDomain();

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
    public Double add(Double t1, Double t2) {
      return t1 + t2;
    }

    @Override
    public Double multiply(Double t1, Double t2) {
      return t1 * t2;
    }

    @Override
    public Double get(long n) {
      return (double) n;
    }
  }
}
