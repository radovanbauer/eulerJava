package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class BigFraction implements Comparable<BigFraction> {
  public abstract BigInteger n();
  public abstract BigInteger d();

  public static BigFraction ZERO = create(BigInteger.ZERO, BigInteger.ONE);
  public static BigFraction ONE = create(BigInteger.ONE, BigInteger.ONE);

  public static BigFraction create(long n) {
    return create(n, 1);
  }

  public static BigFraction create(long n, long d) {
    return create(BigInteger.valueOf(n), BigInteger.valueOf(d));
  }

  public static BigFraction create(LongFraction lf) {
    return create(lf.n(), lf.d());
  }

  public static BigFraction create(BigInteger n, BigInteger d) {
    checkArgument(d.compareTo(BigInteger.ZERO) != 0);
    if (d.compareTo(BigInteger.ZERO) < 0) {
      n = n.negate();
      d = d.negate();
    }
    BigInteger gcd = n.abs().gcd(d);
    return new AutoValue_BigFraction(n.divide(gcd), d.divide(gcd));
  }

  public static BigFraction create(BigInteger n) {
    return create(n, BigInteger.ONE);
  }

  public BigFraction add(BigFraction that) {
    return create(
        this.n().multiply(that.d()).add(that.n().multiply(this.d())),
        this.d().multiply(that.d()));
  }

  public BigFraction add(long that) {
    return add(BigFraction.create(that));
  }

  public BigFraction subtract(BigFraction that) {
    return create(
        this.n().multiply(that.d()).subtract(that.n().multiply(this.d())),
        this.d().multiply(that.d()));
  }

  public BigFraction subtract(long that) {
    return subtract(BigFraction.create(that));
  }

  public BigFraction multiply(BigFraction that) {
    return create(this.n().multiply(that.n()), this.d().multiply(that.d()));
  }

  public BigFraction multiply(long that) {
    return multiply(BigFraction.create(that));
  }

  public BigFraction divide(BigFraction that) {
    return create(this.n().multiply(that.d()), this.d().multiply(that.n()));
  }

  public BigFraction divide(long that) {
    return divide(BigFraction.create(that));
  }

  public BigFraction negate() {
    return create(n().negate(), d());
  }

  public BigFraction invert() {
    return create(d(), n());
  }

  public boolean isZero() {
    return this.equals(ZERO);
  }

  public double doubleValue() {
    return new BigDecimal(n()).divide(new BigDecimal(d()), MathContext.DECIMAL128).doubleValue();
  }

  @Override
  public int compareTo(BigFraction that) {
    return this.n().multiply(that.d()).compareTo(that.n().multiply(this.d()));
  }

  @Override
  public String toString() {
    return d().equals(BigInteger.ONE) ? n().toString() : n() + "/" + d();
  }
}
