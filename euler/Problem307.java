package euler;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Problem307 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem307().solve2());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public String solve() {
    int n = 1_000_000;
    int k = 20_000;
    BigDecimal nMinusKFact = BigDecimal.ONE;
    for (int i = 0; i < k; i++) {
      nMinusKFact = nMinusKFact.multiply(BigDecimal.valueOf(n - i));
    }
    BigDecimal x = nMinusKFact.divide(BigDecimal.valueOf(n).pow(k), MathContext.DECIMAL128);
    BigDecimal res = BigDecimal.ONE;
    for (int i = 0; 2 * i <= k; i++) {
      res = res.subtract(x);
      x = x.multiply(BigDecimal.valueOf(2 * i - k).multiply(BigDecimal.valueOf(2 * i - k + 1)))
          .divide(BigDecimal.valueOf(2 * (i + 1)).multiply(BigDecimal.valueOf(i - k + n + 1)), MathContext.DECIMAL128);
    }
    return String.format("%.10f", res);
  }

  public String solve2() {
    int n = 1_000_000;
    int k = 20_000;
    double x = 1;
    for (int i = 0; i < k; i++) {
      x *= 1D * (n - i) / n;
    }
    double res = 1D;
    for (int i = 0; 2 * i <= k; i++) {
      res -= x;
      x *= 1D * (2 * i - k) * (2 * i - k + 1) / (2 * (i + 1) * (i - k + n + 1));
    }
    return String.format("%.10f", res);
  }
}
