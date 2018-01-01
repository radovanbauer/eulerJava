package euler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.BigIntegerMath;

public class Problem398 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem398().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public String solve() {
    int n = 10_000_000;
    int m = 100;
    BigInteger sum = BigInteger.ZERO;
    for (int min = 1; min * m <= n; min++) {
      sum = sum.add(BigInteger.valueOf(min)
          .multiply(binomial(n - m * (min - 1) - 1, m - 1)
              .subtract(binomial(n - m * min - 1, m - 1))
              .subtract(BigInteger.valueOf(m)
                  .multiply(binomial(n - min - (m - 1) * min - 1, m - 2)))));
    }
    for (int min2 = 2; min2 * (m - 1) + 1 <= n; min2++) {
      sum = sum.add(BigInteger.valueOf(m).multiply(BigInteger.valueOf(min2))
          .multiply(binomial(n - (m - 1) * (min2 - 1) - 1, m - 1)
              .subtract(binomial(n - m * (min2 - 1) - 1, m - 1))
              .subtract(binomial(n - (m - 1) * min2 - 1, m - 1))
              .add(binomial(n - m * min2, m - 1))));
    }
    return String.format("%.5f", new BigDecimal(sum)
        .divide(new BigDecimal(BigIntegerMath.binomial(n - 1, m - 1)), MathContext.DECIMAL128));
  }

  private BigInteger binomial(int n, int k) {
    if (n < 0 || k < 0 || k > n) {
      return BigInteger.ZERO;
    }
    return BigIntegerMath.binomial(n, k);
  }
}
