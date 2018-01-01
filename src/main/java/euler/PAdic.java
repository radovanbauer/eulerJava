package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;
import com.google.common.primitives.Longs;

@AutoValue
public abstract class PAdic {
  public abstract long p();
  public abstract int ord();
  public abstract ImmutableList<Long> a();

  static PAdic zero(long p) {
    return new AutoValue_PAdic(p, 0, ImmutableList.of());
  }

  static PAdic create(long p, int ord, Iterable<Long> a, int maxexp) {
    ImmutableList<Long> alist = ImmutableList.copyOf(a);
    List<Long> aa = new ArrayList<>();
    int exp = ord;
    int actualOrd = ord;
    long carry = 0;
    while ((exp - ord < alist.size() || carry != 0) && exp <= maxexp) {
      long elem = 0;
      if (exp - ord < alist.size()) {
        elem = alist.get(exp - ord);
      }
      long d = LongMath.mod(elem + carry, p);
      if (aa.isEmpty() && d == 0) {
        actualOrd++;
      } else {
        aa.add(d);
        carry = (elem + carry - d) / p;
      }
      exp++;
    }
    if (aa.isEmpty()) {
      actualOrd = 0;
    }
    return new AutoValue_PAdic(p, actualOrd, ImmutableList.copyOf(aa));
  }

  static PAdic create(long p, long n, int maxexp) {
    return create(p, LongFraction.create(n), maxexp);
  }

  static PAdic create(long p, LongFraction n, int maxexp) {
    return create(p, BigFraction.create(n), maxexp);
  }

  static PAdic create(long p, BigFraction n, int maxexp) {
    int ord = 0;
    while (n.n().mod(BigInteger.valueOf(p)).compareTo(BigInteger.ZERO) == 0) {
      ord++;
      n = n.divide(p);
    }
    while (n.d().mod(BigInteger.valueOf(p)).compareTo(BigInteger.ZERO) == 0) {
      ord--;
      n = n.multiply(p);
    }
    ImmutableList.Builder<Long> a = ImmutableList.builder();
    int curexp = ord;
    while (curexp <= maxexp && !n.equals(BigFraction.ZERO)) {
      long d = BigMod.create(n.n(), BigInteger.valueOf(p)).divide(n.d()).n().longValueExact();
      a.add(d);
      n = n.subtract(d).divide(p);
      curexp++;
    }
    return new AutoValue_PAdic(p, ord, a.build());
  }

  public BigFraction asBigFraction() {
    BigFraction pPow;
    if (ord() >= 0) {
      pPow = BigFraction.create(BigInteger.valueOf(p()).pow(ord()));
    } else {
      pPow = BigFraction.create(BigInteger.ONE, BigInteger.valueOf(p()).pow(-ord()));
    }
    BigFraction res = BigFraction.ZERO;
    for (long a : a()) {
      res = res.add(pPow.multiply(a));
      pPow = pPow.multiply(p());
    }
    return res;
  }

  public long a(int exp) {
    int idx = exp - ord();
    if (idx >= 0 && idx < a().size()) {
      return a().get(idx);
    } else {
      return 0L;
    }
  }

  public PAdic add(PAdic that, int maxexp) {
    checkCompatible(that);
    List<Long> res = new ArrayList<>();
    int ord = Math.min(this.ord(), that.ord());
    if (maxexp < ord) {
      return zero(this.p());
    }
    for (int exp = ord; exp <= maxexp; exp++) {
      res.add(this.a(exp) + that.a(exp));
    }
    return create(this.p(), ord, res, maxexp);
  }

  public PAdic subtract(PAdic that, int maxexp) {
    checkCompatible(that);
    int ord = Math.min(this.ord(), that.ord());
    if (maxexp < ord) {
      return zero(this.p());
    }
    long[] res = new long[maxexp - ord + 1];
    for (int exp = ord; exp <= maxexp; exp++) {
      res[exp - ord] = this.a(exp) - that.a(exp);
    }
    return create(this.p(), ord, Longs.asList(res), maxexp);
  }

  public PAdic multiply(PAdic that, int maxexp) {
    checkCompatible(that);
    int ord = Math.min(this.ord(), that.ord());
    if (maxexp < ord) {
      return zero(this.p());
    }
    long[] res = new long[maxexp - ord + 1];
    for (int exp = ord; exp <= maxexp; exp++) {
      for (int thisexp = ord; thisexp <= maxexp; thisexp++) {
        int thatexp = exp - thisexp;
        res[exp - ord] += this.a(thisexp) * that.a(thatexp);
      }
    }
    return create(this.p(), ord, Longs.asList(res), maxexp);
  }

  public PAdic shift(int exp, int maxexp) {
    return create(p(), ord() + exp, a(), maxexp);
  }

  private void checkCompatible(PAdic that) {
    checkArgument(this.p() == that.p());
  }
}
