package euler;

import com.google.auto.value.AutoValue;
import com.google.common.base.Preconditions;

/**
 * Represents {@code a + b*sqrt(s)}.
 */
@AutoValue
public abstract class SqrtNumber {

  public abstract BigFraction a();
  public abstract BigFraction b();
  public abstract BigFraction s();

  public static SqrtNumber create(BigFraction a, BigFraction b, BigFraction s) {
    return new AutoValue_SqrtNumber(a,  b, s);
  }

  public static SqrtNumber create(long a, long b, long s) {
    return create(BigFraction.create(a), BigFraction.create(b), BigFraction.create(s));
  }

  public static SqrtNumber zero(long s) {
    return create(0, 0, s);
  }

  public SqrtNumber add(SqrtNumber that) {
    checkCompatible(that);
    return create(this.a().add(that.a()), this.b().add(that.b()), this.s());
  }

  public SqrtNumber subtract(SqrtNumber that) {
    checkCompatible(that);
    return create(this.a().subtract(that.a()), this.b().subtract(that.b()), this.s());
  }

  public SqrtNumber multiply(SqrtNumber that) {
    checkCompatible(that);
    return create(
        this.a().multiply(that.a()).add(this.b().multiply(that.b()).multiply(this.s())),
        this.a().multiply(that.b()).add(this.b().multiply(that.a())),
        this.s());
  }

  public SqrtNumber multiply(BigFraction that) {
    return create(this.a().multiply(that), this.b().multiply(that), this.s());
  }

  public SqrtNumber divide(SqrtNumber that) {
    checkCompatible(that);
    return multiply(create(that.a(), that.b().negate(), this.s()))
        .multiply(BigFraction.ONE.divide(that.a().multiply(that.a())
            .subtract(that.b().multiply(that.b()).multiply(this.s()))));
  }

  public SqrtNumber pow(long exp) {
    if (exp == 0) {
      return create(BigFraction.ONE, BigFraction.ZERO, s());
    } else if (exp < 0) {
      return create(BigFraction.ONE, BigFraction.ZERO, s()).divide(pow(-exp));
    } else if (exp % 2 == 0) {
      SqrtNumber x = pow(exp / 2);
      return x.multiply(x);
    } else {
      SqrtNumber x = pow(exp / 2);
      return x.multiply(x).multiply(this);
    }
  }

  private void checkCompatible(SqrtNumber that) {
    Preconditions.checkArgument(s().equals(that.s()),
        "Incompatible sqrt numbers: %s, %s", this, that);
  }

  @Override
  public String toString() {
    return String.format("%s+%s*sqrt(%s)", a(), b(), s());
  }
}
