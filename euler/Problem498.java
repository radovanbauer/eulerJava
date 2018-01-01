package euler;

import java.math.BigInteger;

import com.google.common.math.BigIntegerMath;
import com.google.common.primitives.Ints;

public class Problem498 {

  public static void main(String[] args) {
    System.out.println(new Problem498().solve());
  }

  public long solve() {
    long n = 10_000_000_000_000L;
    long m = 1_000_000_000_000L;
    long d = 10_000L;
    int mod = 999999937;
    return LongMod.create(1, mod).pow(-d + m)
        .multiply(d - m)
        .multiply(n)
        .multiply(binomialMod(m, d, mod))
        .multiply(binomialMod(-1 + n, -1 + m, mod))
        .divide(m)
        .divide(d - n).n();
  }

  private long binomialMod(long n, long k, int p) {
    LongMod result = LongMod.create(1, p);
    while (n > 0) {
      result = result.multiply(
          BigIntegerMath.binomial(Ints.checkedCast(n % p), Ints.checkedCast(k % p))
              .mod(BigInteger.valueOf(p)).longValueExact());
      n /= p;
      k /= p;
    }
    return result.n();
  }
}
