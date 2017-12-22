package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigInteger;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class BigMod {
  public abstract BigInteger n();
  public abstract BigInteger mod();

  public static BigMod create(BigInteger n, BigInteger mod) {
    checkArgument(mod.compareTo(BigInteger.ZERO) > 0);
    return new AutoValue_BigMod(n.mod(mod), mod);
  }

  public static BigMod create(long n, long mod) {
    return create(BigInteger.valueOf(n), BigInteger.valueOf(mod));
  }

  public static BigMod zero(BigInteger mod) {
    return create(BigInteger.ZERO, mod);
  }

  public static BigMod zero(long mod) {
    return create(BigInteger.ZERO, BigInteger.valueOf(mod));
  }

  private void checkCompatible(BigMod that) {
    checkArgument(this.mod().equals(that.mod()));
  }

  public BigMod add(BigMod that) {
    checkCompatible(that);
    return create(this.n().add(that.n()), mod());
  }

  public BigMod add(long that) {
    return add(BigMod.create(BigInteger.valueOf(that), mod()));
  }

  public BigMod add(BigInteger that) {
    return add(BigMod.create(that, mod()));
  }

  public BigMod subtract(BigMod that) {
    checkCompatible(that);
    return create(this.n().subtract(that.n()), mod());
  }

  public BigMod subtract(long that) {
    return subtract(BigMod.create(BigInteger.valueOf(that), mod()));
  }

  public BigMod subtract(BigInteger that) {
    return subtract(BigMod.create(that, mod()));
  }

  public BigMod multiply(BigMod that) {
    checkCompatible(that);
    return create(this.n().multiply(that.n()), mod());
  }

  public BigMod multiply(long that) {
    return multiply(BigMod.create(BigInteger.valueOf(that), mod()));
  }

  public BigMod multiply(BigInteger that) {
    return multiply(BigMod.create(that, mod()));
  }

  public BigMod divide(BigMod that) {
    checkCompatible(that);
    return create(this.n().multiply(that.invert().n()), mod());
  }

  public BigMod divide(long that) {
    return divide(BigMod.create(BigInteger.valueOf(that), mod()));
  }

  public BigMod divide(BigInteger that) {
    return divide(BigMod.create(that, mod()));
  }

  public BigMod negate() {
    return create(this.n().negate(), mod());
  }

  public BigMod invert() {
    return create(this.n().modInverse(mod()), mod());
  }

  public BigMod pow(long exp) {
    return create(this.n().modPow(BigInteger.valueOf(exp), mod()), mod());
  }

  @Override
  public String toString() {
    return n() + "(mod " + mod() + ")";
  }
}
