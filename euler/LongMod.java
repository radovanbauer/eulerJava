package euler;

import com.google.auto.value.AutoValue;
import com.google.common.math.LongMath;

import java.math.BigInteger;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.math.LongMath.*;

@AutoValue
public abstract class LongMod {
  public abstract long n();
  public abstract long mod();

  public static LongMod create(long n, long mod) {
    checkArgument(mod > 0);
    return new AutoValue_LongMod(LongMath.mod(n, mod), mod);
  }

  public static LongMod create(BigInteger n, long mod) {
    checkArgument(mod > 0);
    return create(n.mod(BigInteger.valueOf(mod)).longValueExact(), mod);
  }

  public static LongMod zero(long mod) {
    return create(0, mod);
  }

  private void checkCompatible(LongMod that) {
    checkArgument(this.mod() == that.mod());
  }

  public LongMod add(LongMod that) {
    checkCompatible(that);
    return create(checkedAdd(this.n(), that.n()), mod());
  }

  public LongMod add(long that) {
    return add(LongMod.create(that, mod()));
  }

  public LongMod subtract(LongMod that) {
    checkCompatible(that);
    return create(checkedSubtract(this.n(), that.n()), mod());
  }

  public LongMod subtract(long that) {
    return subtract(LongMod.create(that, mod()));
  }

  public LongMod multiply(LongMod that) {
    checkCompatible(that);
    return create(checkedMultiply(this.n(), that.n()), mod());
  }

  public LongMod multiply(long that) {
    return multiply(LongMod.create(that, mod()));
  }

  public LongMod multiply(BigInteger that) {
    return multiply(LongMod.create(that, mod()));
  }

  public LongMod divide(LongMod that) {
    checkCompatible(that);
    return create(checkedMultiply(this.n(), that.invert().n()), mod());
  }

  public LongMod divide(long that) {
    return divide(LongMod.create(that, mod()));
  }

  public LongMod divide(BigInteger that) {
    return divide(LongMod.create(that, mod()));
  }

  public LongMod negate() {
    return create(-this.n(), mod());
  }

  public LongMod invert() {
    long r1 = mod();
    long r2 = n();
    long s1 = 0;
    long s2 = 1;
    while (true) {
      long q = r1 / r2;
      long r = r1 - q * r2;
      if (r == 0) {
        checkArgument(r2 == 1, "No modular inverse exists for %s", this);
        return create(s2, mod());
      }
      long s = s1 - q * s2;
      r1 = r2; r2 = r;
      s1 = s2; s2 = s;
    }
  }

  public LongMod pow(long exp) {
    if (exp < 0) {
      return invert().pow(-exp);
    }
    return create(powMod(n(), exp, mod()), mod());
  }

  private static long powMod(long n, long exp, long mod) {
    long res = 1L;
    long nPow = n;
    while (exp > 0) {
      if ((exp & 1L) != 0) {
        res = LongMath.mod(checkedMultiply(res, nPow), mod);
      }
      nPow = LongMath.mod(checkedMultiply(nPow, nPow), mod);
      exp >>= 1;
    }
    return LongMath.mod(res, mod);
  }

  // only works for prime mod
  public Optional<LongMod> sqrt() {
    long a = n();
    long p = mod();

    if (a == 0) {
      return Optional.of(zero(p));
    } else if (p == 2) {
      return Optional.of(this);
    } else if (legendreSymbol(a, mod()) == -1) {
      return Optional.empty();
    } else if (p % 4 == 3) {
      return Optional.of(pow((p + 1) / 4));
    }

    long s = p - 1;
    long e = 0;
    while (s % 2 == 0) {
      s /= 2;
      e++;
    }

    long n = 2;
    while (legendreSymbol(n, p) != -1) {
      n++;
    }

    long x = powMod(a, (s + 1) / 2, p);
    long b = powMod(a, s, p);
    long g = powMod(n, s, p);
    long r = e;

    while (true) {
      long t = b;
      long m = 0;
      for (; m < r; m++) {
        if (t == 1) {
          break;
        }
        t = powMod(t, 2, p);
      }

      if (m == 0) {
        return Optional.of(create(x, p));
      }

      long gs = powMod(g, 1L << (r - m - 1), p);
      g = (gs * gs) % p;
      x = (x * gs) % p;
      b = (b * g) % p;
      r = m;
    }
  }

  private static long legendreSymbol(long a, long p) {
    long ls = powMod(a, (p - 1) / 2, p);
    return ls == p - 1 ? -1 : ls;
  }

  @Override
  public String toString() {
    return n() + "(mod " + mod() + ")";
  }
}
