package euler;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.common.math.BigIntegerMath;

public class Problem403 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem403().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long n = 1_000_000_000_000L;
    long mod = 100_000_000L;
    long maxx = BigIntegerMath.sqrt(
        BigInteger.valueOf(n).pow(2)
            .add(BigInteger.valueOf(n).multiply(BigInteger.valueOf(4))),
        RoundingMode.FLOOR).longValueExact();
    LongMod sum = LongMod.zero(mod);
    long x = 0;
    while (x <= maxx) {
      A a = getA(n, x);
      long count = a.count();
      long nextx;
      if (a.mina() <= 1) {
        nextx = x + 1;
      } else {
        long lo = x;
        long hi = maxx + 1;
        while (hi - lo > 1) {
          long mid = (lo + hi) / 2;
          if (getA(n, mid).count() == count) {
            lo = mid;
          } else {
            hi = mid;
          }
        }
        nextx = hi;
      }
      BigInteger dsum = dsum(nextx - 1).subtract(dsum(x - 1));
      sum = sum.add(LongMod.create(count, mod)
          .multiply(dsum.mod(BigInteger.valueOf(mod)).longValueExact()));
      x = nextx;
    }
    return sum.n();
  }

  private BigInteger dsum(long x) {
    BigInteger xbi = BigInteger.valueOf(x);
    return xbi.add(BigInteger.ONE)
        .multiply(xbi.add(BigInteger.valueOf(2)))
        .multiply(xbi.multiply(xbi.subtract(BigInteger.ONE)).add(BigInteger.valueOf(12)))
        .divide(BigInteger.valueOf(24));
  }

  private A getA(long n, long x) {
    BigInteger x2 = BigInteger.valueOf(x).pow(2);
    BigInteger n4 = BigInteger.valueOf(4).multiply(BigInteger.valueOf(n));
    long mina = x2.compareTo(n4) >= 0
        ? BigIntegerMath.sqrt(x2.subtract(n4), RoundingMode.CEILING).longValueExact()
        : 0L;
    if ((mina - x) % 2 != 0) {
      mina++;
    }
    long maxa = Math.min(BigIntegerMath.sqrt(x2.add(n4), RoundingMode.FLOOR).longValueExact(), n);
    if ((maxa - x) % 2 != 0) {
      maxa--;
    }
    return A.create(mina, maxa);
  }

  @AutoValue
  static abstract class A {
    abstract long mina();
    abstract long maxa();

    long count() {
      return maxa() - mina() + 1 + (mina() != 0 ? 1 : 0);
    }

    static A create(long mina, long maxa) {
      return new AutoValue_Problem403_A(mina, maxa);
    }
  }
}
