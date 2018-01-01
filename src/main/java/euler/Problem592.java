package euler;

import com.google.common.math.LongMath;

import static java.math.RoundingMode.UNNECESSARY;
import static java.util.Locale.US;

public class Problem592 {

  private static final long MOD = LongMath.pow(16, 12);
  private static final long MODSQRT = LongMath.sqrt(MOD, UNNECESSARY);

  public static void main(String[] args) {
    Runner.run(new Problem592()::solve);
  }

  public String solve() {
    long n = LongMath.factorial(20);

    BigMod factorialOddAll = factorialOdd(MOD);
    BigMod prod = BigMod.create(1, MOD);
    for (long a = 1; a <= n; a *= 2) {
      long div = n / a / MOD;
      long rem = n / a % MOD;
      prod = prod.multiply(factorialOddAll.pow(div));
      prod = prod.multiply(factorialOdd(rem));
    }
    long count2 = 0L;
    for (long a = 2; a <= n; a *= 2) {
      count2 += n / a;
    }
    prod = prod.multiply(BigMod.create(2, MOD).pow(count2 % 4));
    return Long.toHexString(prod.n().longValueExact()).toUpperCase(US);
  }

  private static final BigMod BASE;
  static {
    BigMod prod = BigMod.create(1, MOD);
    for (long i = 1; i <= MODSQRT; i += 2) {
      prod = prod.multiply(i);
    }
    BASE = prod;
  }

  private BigMod factorialOdd(long n) {
    BigMod res = BASE.pow(n / MODSQRT);
    for (long i = n / MODSQRT * MODSQRT + 1; i <= n; i += 2) {
      res = res.multiply(i);
    }
    return res;
  }
}
