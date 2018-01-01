package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.math.LongMath.checkedAdd;
import static com.google.common.math.LongMath.checkedMultiply;
import static com.google.common.math.LongMath.checkedSubtract;
import static com.google.common.math.LongMath.gcd;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class LongFraction implements Comparable<LongFraction> {
  public abstract long n();
  public abstract long d();

  public static LongFraction ZERO = create(0, 1);
  public static LongFraction ONE = create(1, 1);

  public static LongFraction create(long n, long d) {
    checkArgument(d != 0);
    if (d < 0) {
      n *= -1;
      d *= -1;
    }
    long gcd = gcd(Math.abs(n), d);
    return new AutoValue_LongFraction(n / gcd, d / gcd);
  }

  public static LongFraction create(long n) {
    return create(n, 1);
  }

  public LongFraction add(LongFraction that) {
    return create(
        checkedAdd(checkedMultiply(this.n(), that.d()), checkedMultiply(that.n(), this.d())),
        checkedMultiply(this.d(), that.d()));
  }

  public LongFraction add(long that) {
    return add(LongFraction.create(that));
  }

  public LongFraction subtract(LongFraction that) {
    return create(
        checkedSubtract(checkedMultiply(this.n(), that.d()), checkedMultiply(that.n(), this.d())),
        checkedMultiply(this.d(), that.d()));
  }

  public LongFraction multiply(LongFraction that) {
    return create(checkedMultiply(this.n(), that.n()), checkedMultiply(this.d(), that.d()));
  }

  public LongFraction multiply(long that) {
    return multiply(create(that));
  }

  public LongFraction divide(LongFraction that) {
    return create(checkedMultiply(this.n(), that.d()), checkedMultiply(this.d(), that.n()));
  }

  public LongFraction divide(long that) {
    return divide(LongFraction.create(that));
  }

  public LongFraction negate() {
    return create(-n(), d());
  }

  public LongFraction invert() {
    return create(d(), n());
  }

  public boolean isZero() {
    return n() == 0;
  }

  @Override
  public int compareTo(LongFraction that) {
    return Long.compare(checkedMultiply(this.n(), that.d()), checkedMultiply(that.n(), this.d()));
  }

  @Override
  public String toString() {
    if (d() == 1) {
      return "" + n();
    } else {
      return "" + n() + "/" + d();
    }
  }
}
